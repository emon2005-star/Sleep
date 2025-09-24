package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Advanced Rewards Management System
 * Handles all sleep-related rewards including money, XP, items, and effects
 * 
 * @author Turjo
 * @version 1.5.0
 */
public class RewardsManager {
    
    private final EasySleep plugin;
    private final Map<UUID, Integer> playerStreaks;
    private final Map<UUID, Long> lastSleepTime;
    private Economy economy;
    
    public RewardsManager(EasySleep plugin) {
        this.plugin = plugin;
        this.playerStreaks = new HashMap<>();
        this.lastSleepTime = new HashMap<>();
        setupEconomy();
        loadStreakData();
    }
    
    /**
     * Setup Vault economy integration
     */
    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().info("Vault not found - economy rewards disabled");
            return;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().info("No economy plugin found - economy rewards disabled");
            return;
        }
        
        economy = rsp.getProvider();
        plugin.getLogger().info("Economy integration enabled with " + economy.getName());
    }
    
    /**
     * Give all sleep rewards to player
     */
    public void giveSleepRewards(Player player) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.enabled", true)) {
            return;
        }
        
        // Check if player has permission to receive rewards
        if (!player.hasPermission("easysleep.rewards")) {
            return;
        }
        
        // Update streak
        updatePlayerStreak(player);
        int streak = getPlayerStreak(player);
        
        // Calculate multipliers
        double multiplier = calculateMultiplier(player, streak);
        
        // Give rewards
        giveMoneyReward(player, multiplier);
        giveExperienceReward(player, multiplier);
        giveItemRewards(player);
        givePotionEffects(player);
        
        // Award dream coins (integrated into rewards system)
        int baseDreamCoins = plugin.getConfigManager().getConfig().getInt("rewards.economy.dream-coins-per-sleep", 5);
        plugin.getSleepEconomyManager().awardDreamCoins(player, baseDreamCoins);
        
        // Check for streak milestones
        checkStreakMilestones(player, streak);
        
        // Play reward sound
        if (plugin.getConfigManager().getConfig().getBoolean("sounds.enabled", true)) {
            player.playSound(player.getLocation(), 
                org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.5f);
        }
    }
    
    /**
     * Give money reward
     */
    private void giveMoneyReward(Player player, double multiplier) {
        if (economy == null || !plugin.getConfigManager().getConfig().getBoolean("rewards.economy.enabled", true)) {
            return;
        }
        
        double baseAmount = plugin.getConfigManager().getConfig().getDouble("rewards.economy.money-per-sleep", 25.0);
        double amount = baseAmount * multiplier;
        
        try {
            net.milkbowl.vault.economy.EconomyResponse response = economy.depositPlayer(player, amount);
            
            if (response.transactionSuccess()) {
                String message = plugin.getConfigManager().getConfig().getString("messages.rewards.money-received", 
                    "&a+ $%amount% &7(Sleep reward)");
                MessageUtils.sendMessage(player, message.replace("%amount%", String.format("%.2f", amount)));
                
                if (plugin.getConfigManager().isDebugMode()) {
                    plugin.getLogger().info("Successfully deposited $" + amount + " to " + player.getName());
                }
            } else {
                plugin.getLogger().warning("Failed to deposit money to " + player.getName() + ": " + response.errorMessage);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error depositing money to " + player.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Give experience reward
     */
    private void giveExperienceReward(Player player, double multiplier) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.experience.enabled", true)) {
            return;
        }
        
        int baseXP = plugin.getConfigManager().getConfig().getInt("rewards.experience.xp-per-sleep", 100);
        int levels = plugin.getConfigManager().getConfig().getInt("rewards.experience.levels-per-sleep", 0);
        
        int xpAmount = (int) (baseXP * multiplier);
        
        player.giveExp(xpAmount);
        if (levels > 0) {
            player.giveExpLevels(levels);
        }
        
        String message = plugin.getConfigManager().getConfig().getString("messages.rewards.xp-received", 
            "&a+ %amount% XP &7(Sleep reward)");
        MessageUtils.sendMessage(player, message.replace("%amount%", String.valueOf(xpAmount)));
    }
    
    /**
     * Give item rewards
     */
    private void giveItemRewards(Player player) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.items.enabled", true)) {
            return;
        }
        
        List<String> itemList = plugin.getConfigManager().getConfig().getStringList("rewards.items.sleep-rewards");
        
        for (String itemString : itemList) {
            ItemStack item = parseItemString(itemString);
            if (item != null) {
                player.getInventory().addItem(item);
                
                String message = plugin.getConfigManager().getConfig().getString("messages.rewards.item-received", 
                    "&a+ %item% &7(Sleep reward)");
                MessageUtils.sendMessage(player, message.replace("%item%", 
                    item.getAmount() + "x " + item.getType().name().toLowerCase().replace("_", " ")));
            }
        }
    }
    
    /**
     * Give potion effects
     */
    private void givePotionEffects(Player player) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.effects.enabled", true)) {
            return;
        }
        
        List<String> effectList = plugin.getConfigManager().getConfig().getStringList("rewards.effects.sleep-effects");
        
        for (String effectString : effectList) {
            PotionEffect effect = parseEffectString(effectString);
            if (effect != null) {
                player.addPotionEffect(effect);
                
                String message = plugin.getConfigManager().getConfig().getString("messages.rewards.effect-received", 
                    "&b+ %effect% &7(Sleep bonus)");
                MessageUtils.sendMessage(player, message.replace("%effect%", 
                    effect.getType().getName().toLowerCase().replace("_", " ")));
            }
        }
    }
    
    /**
     * Update player sleep streak
     */
    private void updatePlayerStreak(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastSleep = lastSleepTime.getOrDefault(uuid, 0L);
        
        // Check if it's been more than 48 hours (2 days)
        long timeDiff = currentTime - lastSleep;
        long twoDays = 48 * 60 * 60 * 1000L;
        
        if (lastSleep == 0 || timeDiff <= twoDays) {
            // Continue or start streak
            int currentStreak = playerStreaks.getOrDefault(uuid, 0);
            playerStreaks.put(uuid, currentStreak + 1);
        } else if (plugin.getConfigManager().getConfig().getBoolean("rewards.streaks.reset-on-miss", false)) {
            // Reset streak if configured to do so
            playerStreaks.put(uuid, 1);
        }
        
        lastSleepTime.put(uuid, currentTime);
        saveStreakData();
    }
    
    /**
     * Calculate reward multiplier based on streak and moon phase
     */
    private double calculateMultiplier(Player player, int streak) {
        double multiplier = 1.0;
        
        // Streak bonus
        if (streak > 1) {
            double streakBonus = plugin.getConfigManager().getConfig().getDouble("rewards.economy.bonus-multiplier", 1.5);
            multiplier += (streak - 1) * 0.1; // 10% bonus per streak day
            if (multiplier > streakBonus) {
                multiplier = streakBonus; // Cap the multiplier
            }
        }
        
        // Moon phase bonus
        if (plugin.getConfigManager().getConfig().getBoolean("features.moon-phases.affect-rewards", true)) {
            MoonPhaseManager.MoonPhase moonPhase = plugin.getMoonPhaseManager().getCurrentMoonPhase(player.getWorld());
            if (moonPhase != null) {
                String phaseName = moonPhase.name();
                double phaseMultiplier = plugin.getConfigManager().getConfig().getDouble(
                    "features.moon-phases.bonus-multipliers." + phaseName, 1.0);
                multiplier *= phaseMultiplier;
            }
        }
        
        return multiplier;
    }
    
    /**
     * Check for streak milestones and give special rewards
     */
    private void checkStreakMilestones(Player player, int streak) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.streaks.milestone-rewards", true)) {
            return;
        }
        
        // Check if this streak is a milestone
        if (plugin.getConfigManager().getConfig().contains("rewards.items.streak-rewards." + streak)) {
            List<String> milestoneRewards = plugin.getConfigManager().getConfig().getStringList(
                "rewards.items.streak-rewards." + streak);
            
            for (String itemString : milestoneRewards) {
                ItemStack item = parseItemString(itemString);
                if (item != null) {
                    player.getInventory().addItem(item);
                }
            }
            
            // Announce milestone
            if (plugin.getConfigManager().getConfig().getBoolean("rewards.streaks.announce-milestones", true)) {
                String message = plugin.getConfigManager().getConfig().getString("messages.rewards.streak-milestone", 
                    "&6🏆 &e%days%-day sleep streak! &6Bonus rewards unlocked!");
                MessageUtils.sendMessage(player, message.replace("%days%", String.valueOf(streak)));
                
                // Special effects for milestones
                player.getWorld().spawnParticle(org.bukkit.Particle.TOTEM, 
                    player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
                player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }
    }
    
    /**
     * Parse item string format: "MATERIAL:AMOUNT" or "MATERIAL:AMOUNT:DATA"
     */
    private ItemStack parseItemString(String itemString) {
        try {
            String[] parts = itemString.split(":");
            Material material = Material.valueOf(parts[0].toUpperCase());
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            
            return new ItemStack(material, amount);
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid item format: " + itemString);
            return null;
        }
    }
    
    /**
     * Parse effect string format: "EFFECT_NAME:DURATION:AMPLIFIER"
     */
    private PotionEffect parseEffectString(String effectString) {
        try {
            String[] parts = effectString.split(":");
            PotionEffectType effectType = PotionEffectType.getByName(parts[0].toUpperCase());
            int duration = parts.length > 1 ? Integer.parseInt(parts[1]) * 20 : 600; // Convert to ticks
            int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            
            if (effectType != null) {
                return new PotionEffect(effectType, duration, amplifier);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid effect format: " + effectString);
        }
        return null;
    }
    
    /**
     * Get player's current sleep streak
     */
    public int getPlayerStreak(Player player) {
        return playerStreaks.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Reset player's sleep streak
     */
    public void resetPlayerStreak(Player player) {
        playerStreaks.put(player.getUniqueId(), 0);
        saveStreakData();
    }
    
    /**
     * Load streak data from file
     */
    private void loadStreakData() {
        // Implementation would load from data file
        // For now, streaks reset on server restart
    }
    
    /**
     * Save streak data to file
     */
    private void saveStreakData() {
        // Implementation would save to data file
        // For now, streaks are stored in memory only
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        saveStreakData();
        playerStreaks.clear();
        lastSleepTime.clear();
    }
}