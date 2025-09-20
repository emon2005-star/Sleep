package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages plugin configuration and settings with hot-reload capability
 * 
 * @author Turjo
 * @version 1.5.1
 */
public class ConfigManager {
    
    private final EasySleep plugin;
    private FileConfiguration config;
    
    public ConfigManager(EasySleep plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    /**
     * Load configuration with defaults
     */
    private void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        
        // Ensure config version is up to date
        if (config.getInt("config-version", 0) < 16) {
            plugin.getLogger().info("Updating configuration to version 16...");
            updateConfigToV16();
        }
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    /**
     * Update configuration to version 16
     */
    private void updateConfigToV16() {
        // Migrate old settings to new structure
        if (config.contains("settings.default-sleep-percentage")) {
            config.set("sleep.default-percentage", config.getInt("settings.default-sleep-percentage", 50));
            config.set("settings.default-sleep-percentage", null);
        }
        
        // Set new defaults
        config.addDefault("sleep.default-percentage", 50);
        config.addDefault("sleep.time-acceleration", 1.75);
        config.addDefault("rewards.enabled", true);
        config.addDefault("rewards.economy.enabled", true);
        config.addDefault("rewards.economy.money-per-sleep", 25.0);
        config.addDefault("animations.enabled", true);
        config.addDefault("animations.gentle-mode", true);
        config.addDefault("animations.sleep-particle", "SOUL_FIRE_FLAME");
        config.addDefault("sounds.sleep-sound.sound", "BLOCK_AMETHYST_BLOCK_CHIME");
        config.addDefault("daily-messages.enabled", true);
        
        config.set("config-version", 16);
        config.addDefault("messages.enabled.sleep-messages", true);
        config.addDefault("messages.enabled.night-skip-messages", true);
        config.addDefault("messages.enabled.time-acceleration", true);
        config.addDefault("messages.enabled.reward-messages", true);
        config.addDefault("messages.enabled.dream-messages", true);
        config.addDefault("messages.enabled.ritual-messages", true);
        config.addDefault("messages.enabled.moon-phase-messages", true);
        config.addDefault("messages.enabled.day-counter-messages", true);
        config.addDefault("messages.enabled.command-responses", true);
        config.addDefault("messages.enabled.error-messages", true);
        config.addDefault("messages.enabled.achievement-messages", true);
        config.addDefault("messages.enabled.economy-messages", true);
        config.addDefault("messages.enabled.quantum-messages", true);
        config.addDefault("messages.enabled.dimensional-messages", true);
        
        config.set("config-version", 16);
        plugin.saveConfig();
    }
    
    /**
     * Get the configuration object
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * Check if animations are enabled
     */
    public boolean areAnimationsEnabled() {
        return config.getBoolean("animations.enabled", true);
    }
    
    /**
     * Check if sound effects are enabled
     */
    public boolean areSoundEffectsEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }
    
    /**
     * Check if enhanced particles are enabled
     */
    public boolean areEnhancedParticlesEnabled() {
        return config.getBoolean("animations.enhanced-particles", true);
    }
    
    /**
     * Get animation intensity level
     */
    public int getAnimationIntensity() {
        return config.getInt("animations.intensity", 2);
    }
    
    /**
     * Get sound volume multiplier
     */
    public double getSoundVolume() {
        return config.getDouble("sounds.master-volume", 0.3);
    }
    
    /**
     * Get maximum animation distance
     */
    public int getMaxAnimationDistance() {
        return config.getInt("animations.max-distance", 32);
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return config.getBoolean("technical.debug-mode", false);
    }
    
    /**
     * Check if day counter is enabled
     */
    public boolean isDayCounterEnabled() {
        return config.getBoolean("features.day-counter.enabled", true);
    }
    
    /**
     * Check if clock animation is enabled
     */
    public boolean isClockAnimationEnabled() {
        return config.getBoolean("animations.clock-animation", true);
    }
    
    /**
     * Check if day-night animation is enabled
     */
    public boolean isDayNightAnimationEnabled() {
        return config.getBoolean("animations.day-night-cycle", true);
    }
    
    /**
     * Check if anti-spam is enabled
     */
    public boolean isAntiSpamEnabled() {
        return config.getBoolean("features.anti-spam.enabled", true);
    }
    
    /**
     * Check if AFK detection is enabled
     */
    public boolean isAFKDetectionEnabled() {
        return config.getBoolean("features.afk-detection.enabled", true);
    }
    
    /**
     * Get AFK threshold in minutes
     */
    public int getAFKThreshold() {
        return config.getInt("features.afk-detection.threshold-minutes", 5);
    }
    
    /**
     * Get sleep message cooldown in seconds
     */
    public int getSleepMessageCooldown() {
        return config.getInt("features.anti-spam.sleep-message-cooldown", 5);
    }
    
    /**
     * Get wake message cooldown in seconds
     */
    public int getWakeMessageCooldown() {
        return config.getInt("features.anti-spam.wake-message-cooldown", 3);
    }
    
    /**
     * Get command cooldown in seconds
     */
    public int getCommandCooldown() {
        return config.getInt("features.anti-spam.command-cooldown", 2);
    }
    
    /**
     * Get sleep particle type
     */
    public String getSleepParticle() {
        return config.getString("animations.sleep-particle", "SOUL_FIRE_FLAME");
    }
    
    /**
     * Get sleep sound type
     */
    public String getSleepSound() {
        return config.getString("sounds.sleep-sound.sound", "BLOCK_AMETHYST_BLOCK_CHIME");
    }
    
    /**
     * Get random morning messages
     */
    public java.util.List<String> getRandomMorningMessages() {
        return config.getStringList("daily-messages.messages");
    }
    
    /**
     * Check if specific message category is enabled
     */
    public boolean isMessageCategoryEnabled(String category) {
        return config.getBoolean("messages.enabled." + category, true);
    }
    
    /**
     * Get customizable message with placeholder replacement
     */
    public String getMessage(String path, String... placeholders) {
        String message = config.getString("messages." + path, "");
        
        // Replace placeholders in pairs (key, value)
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        
        return message;
    }
    
    /**
     * Get decorative border line
     */
    public String getBorderLine() {
        return config.getString("messages.decorations.border-line", 
            "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    /**
     * Get section separator
     */
    public String getSectionSeparator() {
        return config.getString("messages.decorations.section-separator",
            "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
    
    /**
     * Check if performance mode is enabled
     */
    public boolean isPerformanceMode() {
        return config.getBoolean("animations.performance-mode", false);
    }
    
    /**
     * Reload configuration
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        // Restart clock animation if needed
        if (plugin.getClockAnimationManager() != null) {
            plugin.getClockAnimationManager().restart();
        }
        
        plugin.getLogger().info("Configuration reloaded successfully");
    }
}