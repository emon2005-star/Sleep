package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages plugin configuration and settings with hot-reload capability
 * 
 * @author Turjo
 * @version 1.5.0
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
        if (config.getInt("config-version", 0) < 15) {
            plugin.getLogger().info("Updating configuration to version 15...");
            updateConfigToV15();
        }
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    /**
     * Update configuration to version 15
     */
    private void updateConfigToV15() {
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
        config.addDefault("daily-messages.enabled", true);
        
        config.set("config-version", 15);
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