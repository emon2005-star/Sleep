package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 🏆 SLEEP ACHIEVEMENT MANAGER - EXCLUSIVE ACHIEVEMENT SYSTEM 🏆
 * Revolutionary sleep-based achievement system with unique rewards
 * 
 * @author Turjo
 * @version 1.5.2
 */
public class SleepAchievementManager {
    
    private final EasySleep plugin;
    private final Map<UUID, Set<String>> playerAchievements;
    private final Map<UUID, Map<String, Integer>> playerProgress;
    private File achievementFile;
    private FileConfiguration achievementConfig;
    
    // Exclusive sleep achievements
    private enum SleepAchievement {
        FIRST_DREAM("First Dream", "Sleep for the first time", 1, "GOLDEN_APPLE:1"),
        DREAM_WALKER("Dream Walker", "Sleep 10 times", 10, "DIAMOND:3"),
        NIGHT_GUARDIAN("Night Guardian", "Sleep 50 times", 50, "NETHERITE_INGOT:1"),
        SLEEP_MASTER("Sleep Master", "Sleep 100 times", 100, "BEACON:1"),
        DREAM_ARCHITECT("Dream Architect", "Sleep 500 times", 500, "NETHER_STAR:5"),
        
        STREAK_STARTER("Streak Starter", "Maintain 7-day sleep streak", 7, "EMERALD_BLOCK:2"),
        STREAK_CHAMPION("Streak Champion", "Maintain 30-day sleep streak", 30, "DIAMOND_BLOCK:5"),
        STREAK_LEGEND("Streak Legend", "Maintain 100-day sleep streak", 100, "ENCHANTED_GOLDEN_APPLE:10"),
        
        RITUAL_INITIATE("Ritual Initiate", "Participate in 5 sleep rituals", 5, "SOUL_TORCH:10"),
        RITUAL_MASTER("Ritual Master", "Participate in 25 sleep rituals", 25, "SOUL_LANTERN:5"),
        
        QUANTUM_EXPLORER("Quantum Explorer", "Experience 10 quantum entanglements", 10, "END_CRYSTAL:3"),
        DIMENSIONAL_TRAVELER("Dimensional Traveler", "Sleep in 3 different dimensions", 3, "ENDER_PEARL:16"),
        
        MOON_BLESSED("Moon Blessed", "Sleep during all 8 moon phases", 8, "GLOWSTONE:32"),
        LUNAR_CHAMPION("Lunar Champion", "Sleep during 10 full moons", 10, "BEACON:2"),
        
        DREAM_COLLECTOR("Dream Collector", "Experience all 6 dream types", 6, "TOTEM_OF_UNDYING:1"),
        SLEEP_SAGE("Sleep Sage", "Achieve all other sleep achievements", 1, "DRAGON_EGG:1");
        
        private final String name;
        private final String description;
        private final int requirement;
        private final String reward;
        
        SleepAchievement(String name, String description, int requirement, String reward) {
            this.name = name;
            this.description = description;
            this.requirement = requirement;
            this.reward = reward;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getRequirement() { return requirement; }
        public String getReward() { return reward; }
    }
    
    public SleepAchievementManager(EasySleep plugin) {
        this.plugin = plugin;
        this.playerAchievements = new HashMap<>();
        this.playerProgress = new HashMap<>();
        setupAchievementFile();
        loadAchievements();
    }
    
    /**
     * Setup achievement data file
     */
    private void setupAchievementFile() {
        achievementFile = new File(plugin.getDataFolder(), "achievements.yml");
        if (!achievementFile.exists()) {
            try {
                achievementFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create achievement file: " + e.getMessage());
            }
        }
        achievementConfig = YamlConfiguration.loadConfiguration(achievementFile);
    }
    
    /**
     * Load player achievements
     */
    private void loadAchievements() {
        for (String uuidString : achievementConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                Set<String> achievements = new HashSet<>(achievementConfig.getStringList(uuidString + ".achievements"));
                playerAchievements.put(uuid, achievements);
                
                // Load progress
                Map<String, Integer> progress = new HashMap<>();
                if (achievementConfig.contains(uuidString + ".progress")) {
                    for (String key : achievementConfig.getConfigurationSection(uuidString + ".progress").getKeys(false)) {
                        progress.put(key, achievementConfig.getInt(uuidString + ".progress." + key));
                    }
                }
                playerProgress.put(uuid, progress);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in achievements file: " + uuidString);
            }
        }
    }
    
    /**
     * Save achievements to file
     */
    private void saveAchievements() {
        for (Map.Entry<UUID, Set<String>> entry : playerAchievements.entrySet()) {
            String uuidString = entry.getKey().toString();
            achievementConfig.set(uuidString + ".achievements", new ArrayList<>(entry.getValue()));
            
            // Save progress
            Map<String, Integer> progress = playerProgress.get(entry.getKey());
            if (progress != null) {
                for (Map.Entry<String, Integer> progressEntry : progress.entrySet()) {
                    achievementConfig.set(uuidString + ".progress." + progressEntry.getKey(), progressEntry.getValue());
                }
            }
        }
        
        try {
            achievementConfig.save(achievementFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save achievements: " + e.getMessage());
        }
    }
    
    /**
     * 🌟 Check and award sleep achievements
     */
    public void checkSleepAchievements(Player player) {
        // Check permission
        if (!player.hasPermission("easysleep.achievements")) {
            return;
        }
        
        UUID uuid = player.getUniqueId();
        
        // Update sleep count
        incrementProgress(uuid, "sleep_count");
        
        // Check sleep-based achievements
        checkAchievement(player, SleepAchievement.FIRST_DREAM, getProgress(uuid, "sleep_count"));
        checkAchievement(player, SleepAchievement.DREAM_WALKER, getProgress(uuid, "sleep_count"));
        checkAchievement(player, SleepAchievement.NIGHT_GUARDIAN, getProgress(uuid, "sleep_count"));
        checkAchievement(player, SleepAchievement.SLEEP_MASTER, getProgress(uuid, "sleep_count"));
        checkAchievement(player, SleepAchievement.DREAM_ARCHITECT, getProgress(uuid, "sleep_count"));
    }
    
    /**
     * 🌟 Check streak achievements
     */
    public void checkStreakAchievements(Player player, int streak) {
        checkAchievement(player, SleepAchievement.STREAK_STARTER, streak);
        checkAchievement(player, SleepAchievement.STREAK_CHAMPION, streak);
        checkAchievement(player, SleepAchievement.STREAK_LEGEND, streak);
    }
    
    /**
     * 🌟 Check ritual achievements
     */
    public void checkRitualAchievements(Player player) {
        UUID uuid = player.getUniqueId();
        incrementProgress(uuid, "ritual_count");
        
        checkAchievement(player, SleepAchievement.RITUAL_INITIATE, getProgress(uuid, "ritual_count"));
        checkAchievement(player, SleepAchievement.RITUAL_MASTER, getProgress(uuid, "ritual_count"));
    }
    
    /**
     * 🌟 Check quantum achievements
     */
    public void checkQuantumAchievements(Player player) {
        UUID uuid = player.getUniqueId();
        incrementProgress(uuid, "quantum_count");
        
        checkAchievement(player, SleepAchievement.QUANTUM_EXPLORER, getProgress(uuid, "quantum_count"));
    }
    
    /**
     * 🌟 Check dimensional achievements
     */
    public void checkDimensionalAchievements(Player player) {
        UUID uuid = player.getUniqueId();
        String worldType = player.getWorld().getEnvironment().name();
        
        Set<String> dimensions = getStringSetProgress(uuid, "dimensions_slept");
        dimensions.add(worldType);
        setStringSetProgress(uuid, "dimensions_slept", dimensions);
        
        checkAchievement(player, SleepAchievement.DIMENSIONAL_TRAVELER, dimensions.size());
    }
    
    /**
     * 🌟 Check moon phase achievements
     */
    public void checkMoonPhaseAchievements(Player player, String moonPhase) {
        UUID uuid = player.getUniqueId();
        
        // Track moon phases experienced
        Set<String> phases = getStringSetProgress(uuid, "moon_phases");
        phases.add(moonPhase);
        setStringSetProgress(uuid, "moon_phases", phases);
        
        checkAchievement(player, SleepAchievement.MOON_BLESSED, phases.size());
        
        // Track full moon sleeps
        if ("FULL_MOON".equals(moonPhase)) {
            incrementProgress(uuid, "full_moon_sleeps");
            checkAchievement(player, SleepAchievement.LUNAR_CHAMPION, getProgress(uuid, "full_moon_sleeps"));
        }
    }
    
    /**
     * 🌟 Check dream type achievements
     */
    public void checkDreamTypeAchievements(Player player, String dreamType) {
        UUID uuid = player.getUniqueId();
        
        Set<String> dreamTypes = getStringSetProgress(uuid, "dream_types");
        dreamTypes.add(dreamType);
        setStringSetProgress(uuid, "dream_types", dreamTypes);
        
        checkAchievement(player, SleepAchievement.DREAM_COLLECTOR, dreamTypes.size());
    }
    
    /**
     * Check if player has earned an achievement
     */
    private void checkAchievement(Player player, SleepAchievement achievement, int currentProgress) {
        UUID uuid = player.getUniqueId();
        String achievementName = achievement.name();
        
        // Check if already earned
        Set<String> earned = playerAchievements.getOrDefault(uuid, new HashSet<>());
        if (earned.contains(achievementName)) {
            return;
        }
        
        // Check if requirement met
        if (currentProgress >= achievement.getRequirement()) {
            awardAchievement(player, achievement);
        }
    }
    
    /**
     * 🌟 Award achievement to player
     */
    private void awardAchievement(Player player, SleepAchievement achievement) {
        UUID uuid = player.getUniqueId();
        
        // Add to earned achievements
        Set<String> earned = playerAchievements.getOrDefault(uuid, new HashSet<>());
        earned.add(achievement.name());
        playerAchievements.put(uuid, earned);
        
        // Spectacular achievement announcement
        if (plugin.getConfigManager().isMessageCategoryEnabled("achievement-messages")) {
            MessageUtils.sendMessage(player, "");
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("decorations.achievement-border"));
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("achievements.unlocked"));
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("decorations.achievement-separator"));
            MessageUtils.sendMessage(player, "&6║ &f" + String.format("%-37s", achievement.getName()) + " &6║");
            MessageUtils.sendMessage(player, "&6║ &7" + String.format("%-37s", achievement.getDescription()) + " &6║");
            MessageUtils.sendMessage(player, plugin.getConfigManager().getMessage("decorations.achievement-footer"));
            MessageUtils.sendMessage(player, "");
        }
        
        // Give reward
        giveAchievementReward(player, achievement.getReward());
        
        // Spectacular effects
        Location loc = player.getLocation().add(0, 2, 0);
        player.getWorld().spawnParticle(Particle.TOTEM, loc, 20, 1.0, 1.0, 1.0, 0.2);
        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 15, 1.5, 1.5, 1.5, 0.1);
        
        // Achievement sound
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 2.0f);
        
        // Broadcast to world
        if (plugin.getConfigManager().isMessageCategoryEnabled("achievement-messages")) {
            String broadcastMessage = plugin.getConfigManager().getMessage("achievements.broadcast-unlock", 
                "%player%", player.getName(), "%achievement%", achievement.getName());
            MessageUtils.broadcastToWorld(player.getWorld(), broadcastMessage);
        }
        
        // Check for Sleep Sage achievement (all achievements unlocked)
        if (earned.size() >= SleepAchievement.values().length - 1) { // -1 because Sleep Sage is the final one
            checkAchievement(player, SleepAchievement.SLEEP_SAGE, 1);
        }
        
        saveAchievements();
    }
    
    /**
     * Give achievement reward
     */
    private void giveAchievementReward(Player player, String rewardString) {
        try {
            String[] parts = rewardString.split(":");
            Material material = Material.valueOf(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            
            org.bukkit.inventory.ItemStack reward = new org.bukkit.inventory.ItemStack(material, amount);
            player.getInventory().addItem(reward);
            
            MessageUtils.sendMessage(player, "&a+ " + amount + "x " + material.name().toLowerCase().replace("_", " ") + " &7(Achievement reward)");
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid achievement reward format: " + rewardString);
        }
    }
    
    /**
     * Get player's achievement progress
     */
    private int getProgress(UUID uuid, String progressType) {
        return playerProgress.getOrDefault(uuid, new HashMap<>()).getOrDefault(progressType, 0);
    }
    
    /**
     * Increment player progress
     */
    private void incrementProgress(UUID uuid, String progressType) {
        Map<String, Integer> progress = playerProgress.getOrDefault(uuid, new HashMap<>());
        progress.put(progressType, progress.getOrDefault(progressType, 0) + 1);
        playerProgress.put(uuid, progress);
    }
    
    /**
     * Get string set progress (for tracking multiple items)
     */
    private Set<String> getStringSetProgress(UUID uuid, String progressType) {
        String data = achievementConfig.getString(uuid.toString() + ".string_progress." + progressType, "");
        Set<String> result = new HashSet<>();
        if (!data.isEmpty()) {
            result.addAll(Arrays.asList(data.split(",")));
        }
        return result;
    }
    
    /**
     * Set string set progress
     */
    private void setStringSetProgress(UUID uuid, String progressType, Set<String> data) {
        String dataString = String.join(",", data);
        achievementConfig.set(uuid.toString() + ".string_progress." + progressType, dataString);
    }
    
    /**
     * Get player's earned achievements
     */
    public Set<String> getPlayerAchievements(Player player) {
        return playerAchievements.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
    
    /**
     * Get achievement count for player
     */
    public int getAchievementCount(Player player) {
        return getPlayerAchievements(player).size();
    }
    
    /**
     * Show player's achievements
     */
    public void showAchievements(Player player) {
        Set<String> earned = getPlayerAchievements(player);
        
        MessageUtils.sendMessage(player, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(player, "&6║ &e🏆 &f&lYOUR SLEEP ACHIEVEMENTS &e🏆 &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &fProgress: &e" + earned.size() + "&7/&e" + SleepAchievement.values().length + " &funlocked &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        
        for (SleepAchievement achievement : SleepAchievement.values()) {
            String status = earned.contains(achievement.name()) ? "&a✓" : "&c✗";
            String name = achievement.getName();
            MessageUtils.sendMessage(player, "&6║ " + status + " &f" + String.format("%-35s", name) + " &6║");
        }
        
        MessageUtils.sendMessage(player, "&6╚═══════════════════════════════════════════╝");
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        saveAchievements();
    }
}