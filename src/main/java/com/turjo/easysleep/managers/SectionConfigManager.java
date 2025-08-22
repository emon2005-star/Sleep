package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages section enable/disable configuration
 * Controls which GUI sections are available
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class SectionConfigManager {
    
    private final EasySleep plugin;
    private File sectionFile;
    private FileConfiguration sectionConfig;
    
    public SectionConfigManager(EasySleep plugin) {
        this.plugin = plugin;
        setupSectionFile();
        loadDefaults();
    }
    
    /**
     * Setup section configuration file
     */
    private void setupSectionFile() {
        sectionFile = new File(plugin.getDataFolder(), "sections.yml");
        if (!sectionFile.exists()) {
            try {
                sectionFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create sections.yml: " + e.getMessage());
            }
        }
        sectionConfig = YamlConfiguration.loadConfiguration(sectionFile);
    }
    
    /**
     * Load default section settings
     */
    private void loadDefaults() {
        // Main sections
        sectionConfig.addDefault("sections.main-menu.enabled", true);
        sectionConfig.addDefault("sections.sleep-settings.enabled", true);
        sectionConfig.addDefault("sections.animation-settings.enabled", true);
        sectionConfig.addDefault("sections.sound-settings.enabled", true);
        sectionConfig.addDefault("sections.day-counter.enabled", true);
        sectionConfig.addDefault("sections.statistics.enabled", true);
        sectionConfig.addDefault("sections.world-management.enabled", true);
        sectionConfig.addDefault("sections.player-management.enabled", true);
        sectionConfig.addDefault("sections.advanced-settings.enabled", true);
        sectionConfig.addDefault("sections.particle-effects.enabled", true);
        sectionConfig.addDefault("sections.anti-spam-settings.enabled", true);
        sectionConfig.addDefault("sections.update-center.enabled", true);
        
        // Section descriptions
        sectionConfig.addDefault("sections.main-menu.description", "Main GUI menu with all options");
        sectionConfig.addDefault("sections.sleep-settings.description", "Configure sleep percentage and requirements");
        sectionConfig.addDefault("sections.animation-settings.description", "Manage visual effects and animations");
        sectionConfig.addDefault("sections.sound-settings.description", "Configure audio effects and volume");
        sectionConfig.addDefault("sections.day-counter.description", "Day tracking and morning messages");
        sectionConfig.addDefault("sections.statistics.description", "View plugin usage statistics");
        sectionConfig.addDefault("sections.world-management.description", "Manage world-specific settings");
        sectionConfig.addDefault("sections.player-management.description", "Player-specific configurations");
        sectionConfig.addDefault("sections.advanced-settings.description", "Advanced plugin configuration");
        sectionConfig.addDefault("sections.particle-effects.description", "Customize particle effects");
        sectionConfig.addDefault("sections.anti-spam-settings.description", "Anti-spam protection settings");
        sectionConfig.addDefault("sections.update-center.description", "Plugin updates and information");
        
        sectionConfig.options().copyDefaults(true);
        saveConfig();
    }
    
    /**
     * Check if a section is enabled
     */
    public boolean isSectionEnabled(String sectionName) {
        return sectionConfig.getBoolean("sections." + sectionName + ".enabled", true);
    }
    
    /**
     * Enable or disable a section
     */
    public void setSectionEnabled(String sectionName, boolean enabled) {
        sectionConfig.set("sections." + sectionName + ".enabled", enabled);
        saveConfig();
    }
    
    /**
     * Get section description
     */
    public String getSectionDescription(String sectionName) {
        return sectionConfig.getString("sections." + sectionName + ".description", "No description available");
    }
    
    /**
     * Get all section names
     */
    public java.util.Set<String> getAllSections() {
        if (sectionConfig.getConfigurationSection("sections") != null) {
            return sectionConfig.getConfigurationSection("sections").getKeys(false);
        }
        return new java.util.HashSet<>();
    }
    
    /**
     * Save configuration
     */
    private void saveConfig() {
        try {
            sectionConfig.save(sectionFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save sections.yml: " + e.getMessage());
        }
    }
    
    /**
     * Reload configuration
     */
    public void reloadConfig() {
        sectionConfig = YamlConfiguration.loadConfiguration(sectionFile);
        loadDefaults();
    }
}