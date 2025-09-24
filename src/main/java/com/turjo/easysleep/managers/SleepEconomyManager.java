package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 💰 SLEEP ECONOMY MANAGER - EXCLUSIVE SLEEP-BASED ECONOMY 💰
 * Revolutionary sleep currency and marketplace system
 * 
 * @author Turjo
 * @version 1.5.2
 */
public class SleepEconomyManager {
    
    private final EasySleep plugin;
    private final Map<UUID, Long> dreamCoins;
    private final Map<UUID, Map<String, Integer>> sleepShopPurchases;
    private File economyFile;
    private FileConfiguration economyConfig;
    
    // Sleep shop items with dream coin prices
    private final Map<String, SleepShopItem> sleepShop = new HashMap<String, SleepShopItem>() {{
        put("dream_boost", new SleepShopItem("Dream Boost", "2x sleep rewards for 1 hour", 50, "POTION:1"));
        put("lunar_blessing", new SleepShopItem("Lunar Blessing", "Permanent moon phase bonus", 200, "NETHER_STAR:1"));
        put("quantum_stabilizer", new SleepShopItem("Quantum Stabilizer", "Guaranteed quantum entanglement", 150, "END_CRYSTAL:1"));
        put("dream_catcher", new SleepShopItem("Dream Catcher", "Collect rare dream fragments", 100, "COBWEB:1"));
        put("sleep_multiplier", new SleepShopItem("Sleep Multiplier", "3x sleep effectiveness", 300, "BEACON:1"));
        put("dimensional_key", new SleepShopItem("Dimensional Key", "Access to exclusive dream realms", 500, "ENDER_EYE:1"));
        put("time_crystal", new SleepShopItem("Time Crystal", "Control time acceleration", 750, "DIAMOND_BLOCK:1"));
        put("dream_crown", new SleepShopItem("Dream Crown", "Ultimate sleep mastery item", 1000, "NETHERITE_HELMET:1"));
    }};
    
    public SleepEconomyManager(EasySleep plugin) {
        this.plugin = plugin;
        this.dreamCoins = new HashMap<>();
        this.sleepShopPurchases = new HashMap<>();
        setupEconomyFile();
        loadEconomyData();
    }
    
    /**
     * Setup economy data file
     */
    private void setupEconomyFile() {
        economyFile = new File(plugin.getDataFolder(), "sleep_economy.yml");
        if (!economyFile.exists()) {
            try {
                economyFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create sleep economy file: " + e.getMessage());
            }
        }
        economyConfig = YamlConfiguration.loadConfiguration(economyFile);
    }
    
    /**
     * Load economy data
     */
    private void loadEconomyData() {
        for (String uuidString : economyConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                long coins = economyConfig.getLong(uuidString + ".dream_coins", 0);
                dreamCoins.put(uuid, coins);
                
                // Load purchases
                Map<String, Integer> purchases = new HashMap<>();
                if (economyConfig.contains(uuidString + ".purchases")) {
                    for (String item : economyConfig.getConfigurationSection(uuidString + ".purchases").getKeys(false)) {
                        purchases.put(item, economyConfig.getInt(uuidString + ".purchases." + item));
                    }
                }
                sleepShopPurchases.put(uuid, purchases);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in economy file: " + uuidString);
            }
        }
    }
    
    /**
     * Save economy data
     */
    private void saveEconomyData() {
        for (Map.Entry<UUID, Long> entry : dreamCoins.entrySet()) {
            String uuidString = entry.getKey().toString();
            economyConfig.set(uuidString + ".dream_coins", entry.getValue());
            
            // Save purchases
            Map<String, Integer> purchases = sleepShopPurchases.get(entry.getKey());
            if (purchases != null) {
                for (Map.Entry<String, Integer> purchase : purchases.entrySet()) {
                    economyConfig.set(uuidString + ".purchases." + purchase.getKey(), purchase.getValue());
                }
            }
        }
        
        try {
            economyConfig.save(economyFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save sleep economy: " + e.getMessage());
        }
    }
    
    /**
     * 🌟 Award dream coins for sleeping
     */
    public void awardDreamCoins(Player player, int baseAmount) {
        UUID uuid = player.getUniqueId();
        
        // Calculate bonus multipliers
        int streak = plugin.getRewardsManager().getPlayerStreak(player);
        double multiplier = 1.0 + (streak * 0.1); // 10% bonus per streak day
        
        int finalAmount = (int) (baseAmount * multiplier);
        
        long currentCoins = dreamCoins.getOrDefault(uuid, 0L);
        dreamCoins.put(uuid, currentCoins + finalAmount);
        
        // Announce dream coin reward
        if (plugin.getConfigManager().isMessageCategoryEnabled("economy-messages")) {
            String message = plugin.getConfigManager().getMessage("economy.coins-received", "%amount%", String.valueOf(finalAmount));
            MessageUtils.sendMessage(player, message);
        }
        
        // Spectacular coin effect
        Location loc = player.getLocation().add(0, 1.5, 0);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, finalAmount, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 2.0f);
        
        saveEconomyData();
    }
    
    /**
     * Get player's dream coin balance
     */
    public long getDreamCoins(Player player) {
        return dreamCoins.getOrDefault(player.getUniqueId(), 0L);
    }
    
    /**
     * 🌟 Show sleep shop to player
     */
    public void showSleepShop(Player player) {
        long playerCoins = getDreamCoins(player);
        
        MessageUtils.sendMessage(player, "&d╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(player, "&d║ &e💎 &f&lDREAM COIN SHOP &e💎 &d║");
        MessageUtils.sendMessage(player, "&d╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&d║ &fYour Balance: &e" + playerCoins + " &dDream Coins &d║");
        MessageUtils.sendMessage(player, "&d╠═══════════════════════════════════════════╣");
        
        for (Map.Entry<String, SleepShopItem> entry : sleepShop.entrySet()) {
            SleepShopItem item = entry.getValue();
            String affordability = playerCoins >= item.price ? "&a" : "&c";
            MessageUtils.sendMessage(player, "&d║ " + affordability + item.name + " &7- &e" + item.price + " &dcoins &d║");
            MessageUtils.sendMessage(player, "&d║ &7  " + String.format("%-35s", item.description) + " &d║");
        }
        
        MessageUtils.sendMessage(player, "&d╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&d║ &7Use: &e/sleep shop buy <item> &d║");
        MessageUtils.sendMessage(player, "&d╚═══════════════════════════════════════════╝");
    }
    
    /**
     * 🌟 Purchase item from sleep shop
     */
    public boolean purchaseShopItem(Player player, String itemId) {
        SleepShopItem item = sleepShop.get(itemId.toLowerCase());
        if (item == null) {
            MessageUtils.sendMessage(player, "&cItem not found: " + itemId);
            return false;
        }
        
        UUID uuid = player.getUniqueId();
        long playerCoins = getDreamCoins(player);
        
        if (playerCoins < item.price) {
            MessageUtils.sendMessage(player, "&cInsufficient Dream Coins! Need: &e" + item.price + " &cHave: &e" + playerCoins);
            return false;
        }
        
        // Deduct coins
        dreamCoins.put(uuid, playerCoins - item.price);
        
        // Give items and effects based on item type
        giveShopItemReward(player, itemId);
        
        // Track purchase
        Map<String, Integer> purchases = sleepShopPurchases.getOrDefault(uuid, new HashMap<>());
        purchases.put(itemId, purchases.getOrDefault(itemId, 0) + 1);
        sleepShopPurchases.put(uuid, purchases);
        
        // Success message
        if (plugin.getConfigManager().isMessageCategoryEnabled("economy-messages")) {
            String purchaseMessage = plugin.getConfigManager().getMessage("economy.purchase-success", 
                "%item%", item.name, "%price%", String.valueOf(item.price));
            String balanceMessage = plugin.getConfigManager().getMessage("economy.remaining-balance", 
                "%balance%", String.valueOf(getDreamCoins(player)));
            MessageUtils.sendMessage(player, purchaseMessage);
            MessageUtils.sendMessage(player, balanceMessage);
        }
        
        // Purchase effects
        Location loc = player.getLocation().add(0, 1.5, 0);
        player.getWorld().spawnParticle(Particle.TOTEM, loc, 10, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.8f, 1.5f);
        
        saveEconomyData();
        return true;
    }
    
    /**
     * Give shop item rewards with custom effects
     */
    private void giveShopItemReward(Player player, String itemId) {
        switch (itemId.toLowerCase()) {
            case "food_bundle":
                player.getInventory().addItem(
                    new org.bukkit.inventory.ItemStack(Material.BREAD, 16),
                    new org.bukkit.inventory.ItemStack(Material.COOKED_BEEF, 8),
                    new org.bukkit.inventory.ItemStack(Material.GOLDEN_CARROT, 4)
                );
                break;
                
            case "tool_kit":
                player.getInventory().addItem(
                    new org.bukkit.inventory.ItemStack(Material.IRON_PICKAXE, 1),
                    new org.bukkit.inventory.ItemStack(Material.IRON_AXE, 1),
                    new org.bukkit.inventory.ItemStack(Material.IRON_SHOVEL, 1),
                    new org.bukkit.inventory.ItemStack(Material.IRON_SWORD, 1)
                );
                break;
                
            case "armor_set":
                // Create enchanted iron armor
                org.bukkit.inventory.ItemStack helmet = new org.bukkit.inventory.ItemStack(Material.IRON_HELMET);
                org.bukkit.inventory.ItemStack chestplate = new org.bukkit.inventory.ItemStack(Material.IRON_CHESTPLATE);
                org.bukkit.inventory.ItemStack leggings = new org.bukkit.inventory.ItemStack(Material.IRON_LEGGINGS);
                org.bukkit.inventory.ItemStack boots = new org.bukkit.inventory.ItemStack(Material.IRON_BOOTS);
                
                helmet.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                chestplate.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                leggings.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                boots.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                
                helmet.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 2);
                chestplate.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 2);
                leggings.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 2);
                boots.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 2);
                
                player.getInventory().addItem(helmet, chestplate, leggings, boots);
                break;
                
            case "building_blocks":
                player.getInventory().addItem(
                    new org.bukkit.inventory.ItemStack(Material.STONE_BRICKS, 64),
                    new org.bukkit.inventory.ItemStack(Material.OAK_PLANKS, 32),
                    new org.bukkit.inventory.ItemStack(Material.GLASS, 16),
                    new org.bukkit.inventory.ItemStack(Material.TORCH, 8)
                );
                break;
                
            case "potion_bundle":
                // Create custom potions
                org.bukkit.inventory.ItemStack healingPotion = new org.bukkit.inventory.ItemStack(Material.POTION, 3);
                org.bukkit.inventory.meta.PotionMeta healingMeta = (org.bukkit.inventory.meta.PotionMeta) healingPotion.getItemMeta();
                healingMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HEAL, 1, 1), true);
                healingPotion.setItemMeta(healingMeta);
                
                org.bukkit.inventory.ItemStack speedPotion = new org.bukkit.inventory.ItemStack(Material.POTION, 2);
                org.bukkit.inventory.meta.PotionMeta speedMeta = (org.bukkit.inventory.meta.PotionMeta) speedPotion.getItemMeta();
                speedMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 3600, 0), true);
                speedPotion.setItemMeta(speedMeta);
                
                player.getInventory().addItem(healingPotion, speedPotion);
                break;
                
            case "dream_boost":
                // Give potion and effect
                org.bukkit.inventory.ItemStack dreamBoost = new org.bukkit.inventory.ItemStack(Material.POTION, 1);
                org.bukkit.inventory.meta.PotionMeta dreamMeta = (org.bukkit.inventory.meta.PotionMeta) dreamBoost.getItemMeta();
                dreamMeta.setDisplayName("§d§lDream Boost Potion");
                dreamBoost.setItemMeta(dreamMeta);
                player.getInventory().addItem(dreamBoost);
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, 1200, 1));
                break;
                
            case "lunar_blessing":
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 1));
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION, 12000, 0));
                break;
                
            case "quantum_stabilizer":
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.END_CRYSTAL, 1));
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 6000, 1));
                break;
                
            case "time_crystal":
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_BLOCK, 1));
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING, 12000, 1));
                break;
                
            default:
                // Fallback to old system
                try {
                    SleepShopItem item = sleepShop.get(itemId);
                    if (item != null) {
                        String[] parts = item.reward.split(":");
                        Material material = Material.valueOf(parts[0]);
                        int amount = Integer.parseInt(parts[1]);
                        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Invalid shop item: " + itemId);
                }
                break;
        }
    }
    
    /**
     * Get available shop items
     */
    public Set<String> getShopItems() {
        return sleepShop.keySet();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        saveEconomyData();
    }
    
    /**
     * Sleep shop item data class
     */
    private static class SleepShopItem {
        final String name;
        final String description;
        final int price;
        final String reward;
        
        SleepShopItem(String name, String description, int price, String reward) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.reward = reward;
        }
    }
}