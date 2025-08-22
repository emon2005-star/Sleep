package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages plugin configuration and settings with hot-reload capability
 * 
 * @author Turjo
 * @version 1.3.1
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
        
        // Set default values if not present
        config.addDefault("settings.default-sleep-percentage", 1);
        config.addDefault("settings.enable-animations", true);
        config.addDefault("settings.enable-sound-effects", true);
        config.addDefault("settings.broadcast-sleep-messages", true);
        config.addDefault("settings.auto-configure-new-worlds", true);
        config.addDefault("advanced.animation-intensity", 2);
        config.addDefault("advanced.sound-volume", 1.0);
        config.addDefault("advanced.max-animation-distance", 32);
        config.addDefault("advanced.debug-mode", false);
        config.addDefault("features.day-counter", true);
        config.addDefault("features.enhanced-particles", true);
        config.addDefault("features.weather-effects", false);
        config.addDefault("effects.sleep-particle", "HEART");
        config.addDefault("effects.awake-particle", "ENCHANTMENT_TABLE");
        config.addDefault("effects.sleep-sound", "BLOCK_AMETHYST_BLOCK_CHIME");
        config.addDefault("effects.night-skip-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    /**
     * Get default sleep percentage
     */
    public int getDefaultSleepPercentage() {
        return config.getInt("settings.default-sleep-percentage", 1);
    }
    
    /**
     * Check if animations are enabled
     */
    public boolean areAnimationsEnabled() {
        return config.getBoolean("settings.enable-animations", true);
    }
    
    /**
     * Check if sound effects are enabled
     */
    public boolean areSoundEffectsEnabled() {
        return config.getBoolean("settings.enable-sound-effects", true);
    }
    
    /**
     * Check if sleep messages should be broadcast
     */
    public boolean shouldBroadcastSleepMessages() {
        return config.getBoolean("settings.broadcast-sleep-messages", true);
    }
    
    /**
     * Check if new worlds should be auto-configured
     */
    public boolean shouldAutoConfigureNewWorlds() {
        return config.getBoolean("settings.auto-configure-new-worlds", true);
    }
    
    /**
     * Get animation intensity level
     */
    public int getAnimationIntensity() {
        return config.getInt("advanced.animation-intensity", 2);
    }
    
    /**
     * Get sound volume multiplier
     */
    public double getSoundVolume() {
        return config.getDouble("advanced.sound-volume", 1.0);
    }
    
    /**
     * Get maximum animation distance
     */
    public int getMaxAnimationDistance() {
        return config.getInt("advanced.max-animation-distance", 32);
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return config.getBoolean("advanced.debug-mode", false);
    }
    
    /**
     * Check if day counter is enabled
     */
    public boolean isDayCounterEnabled() {
        return config.getBoolean("features.day-counter", true);
    }
    
    /**
     * Get random morning messages list
     */
    public java.util.List<String> getRandomMorningMessages() {
        return config.getStringList("features.random-morning-messages");
    }
    
    /**
     * Check if enhanced particles are enabled
     */
    public boolean areEnhancedParticlesEnabled() {
        return config.getBoolean("features.enhanced-particles", true);
    }
    
    /**
     * Check if weather effects are enabled
     */
    public boolean areWeatherEffectsEnabled() {
        return config.getBoolean("features.weather-effects", false);
    }
    
    /**
     * Get sleep particle type
     */
    public String getSleepParticle() {
        return config.getString("effects.sleep-particle", "HEART");
    }
    
    /**
     * Get awake particle type
     */
    public String getAwakeParticle() {
        return config.getString("effects.awake-particle", "ENCHANTMENT_TABLE");
    }
    
    /**
     * Get sleep sound
     */
    public String getSleepSound() {
        return config.getString("effects.sleep-sound", "BLOCK_AMETHYST_BLOCK_CHIME");
    }
    
    /**
     * Get night skip sound
     */
    public String getNightSkipSound() {
        return config.getString("effects.night-skip-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
    }
    
    /**
     * Get particle density multiplier
     */
    public double getParticleDensity() {
        return config.getDouble("effects.particle-density", 1.0);
    }
    
    /**
     * Get animation duration in seconds
     */
    public int getAnimationDuration() {
        return config.getInt("advanced.animation-duration", 3);
    }
    
    /**
     * Check if performance mode is enabled
     */
    public boolean isPerformanceMode() {
        return config.getBoolean("advanced.performance-mode", false);
    }
    
    /**
     * Check if update checking is enabled
     */
    public boolean isUpdateCheckEnabled() {
        return config.getBoolean("features.update-checker", true);
    }
    
    /**
     * Check if AFK detection is enabled
     */
    public boolean isAFKDetectionEnabled() {
        return config.getBoolean("features.afk-detection", true);
    }
    
    /**
     * Get AFK threshold in minutes
     */
    public int getAFKThreshold() {
        return config.getInt("features.afk-threshold", 5);
    }
    
    /**
     * Check if clock animation is enabled
     */
    public boolean isClockAnimationEnabled() {
        return config.getBoolean("features.clock-animation", true);
    }
    
    /**
     * Check if day-night animation is enabled
     */
    public boolean isDayNightAnimationEnabled() {
        return config.getBoolean("features.day-night-animation", true);
    }
    
    /**
     * Check if anti-spam is enabled
     */
    public boolean isAntiSpamEnabled() {
        return config.getBoolean("features.anti-spam", true);
    }
    
    /**
     * Get sleep message cooldown in seconds
     */
    public int getSleepMessageCooldown() {
        return config.getInt("anti-spam.sleep-message-cooldown", 5);
    }
    
    /**
     * Get wake message cooldown in seconds
     */
    public int getWakeMessageCooldown() {
        return config.getInt("anti-spam.wake-message-cooldown", 3);
    }
    
    /**
     * Get command cooldown in seconds
     */
    public int getCommandCooldown() {
        return config.getInt("anti-spam.command-cooldown", 2);
    }
    
    /**
     * Reload configuration
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        // Restart clock animation if needed
        plugin.getClockAnimationManager().restart();
        
        plugin.getLogger().info("Configuration reloaded successfully");
    }
}