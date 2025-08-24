package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Manages GUI configuration and customization
 * Controls GUI appearance, items, and sounds
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class GUIConfigManager {
    
    private final EasySleep plugin;
    private File guiFile;
    private FileConfiguration guiConfig;
    
    public GUIConfigManager(EasySleep plugin) {
        this.plugin = plugin;
        setupGUIFile();
        loadDefaults();
    }
    
    /**
     * Setup GUI configuration file
     */
    private void setupGUIFile() {
        guiFile = new File(plugin.getDataFolder(), "gui.yml");
        if (!guiFile.exists()) {
            try {
                guiFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create gui.yml: " + e.getMessage());
            }
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
    }
    
    /**
     * Load default GUI settings
     */
    private void loadDefaults() {
        // GUI Titles
        guiConfig.addDefault("gui.titles.main-menu", "&6&l⚡ EasySleep Control Panel ⚡");
        guiConfig.addDefault("gui.titles.sleep-settings", "&b&l🌙 Sleep Configuration 🌙");
        guiConfig.addDefault("gui.titles.animation-settings", "&d&l✨ Animation Settings ✨");
        guiConfig.addDefault("gui.titles.sound-settings", "&a&l🎵 Sound Configuration 🎵");
        guiConfig.addDefault("gui.titles.day-counter", "&e&l📅 Day Counter Management 📅");
        guiConfig.addDefault("gui.titles.statistics", "&c&l📊 Plugin Statistics 📊");
        guiConfig.addDefault("gui.titles.world-management", "&2&l🌍 World Management 🌍");
        guiConfig.addDefault("gui.titles.player-management", "&9&l👥 Player Management 👥");
        guiConfig.addDefault("gui.titles.advanced-settings", "&4&l⚙️ Advanced Settings ⚙️");
        guiConfig.addDefault("gui.titles.particle-effects", "&5&l🎆 Particle Effects 🎆");
        guiConfig.addDefault("gui.titles.anti-spam-settings", "&c&l🛡️ Anti-Spam Settings 🛡️");
        guiConfig.addDefault("gui.titles.update-center", "&3&l🔄 Update Center 🔄");
        
        // Main Menu Items
        guiConfig.addDefault("gui.items.main-menu.sleep-settings.material", "BED");
        guiConfig.addDefault("gui.items.main-menu.sleep-settings.name", "&b🌙 Sleep Settings");
        guiConfig.addDefault("gui.items.main-menu.sleep-settings.lore", Arrays.asList(
            "&7Configure sleep percentage",
            "&7and requirements for your server",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.animation-settings.material", "FIREWORK_ROCKET");
        guiConfig.addDefault("gui.items.main-menu.animation-settings.name", "&d✨ Animation Settings");
        guiConfig.addDefault("gui.items.main-menu.animation-settings.lore", Arrays.asList(
            "&7Manage visual effects and",
            "&7stunning night skip animations",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.sound-settings.material", "NOTE_BLOCK");
        guiConfig.addDefault("gui.items.main-menu.sound-settings.name", "&a🎵 Sound Settings");
        guiConfig.addDefault("gui.items.main-menu.sound-settings.lore", Arrays.asList(
            "&7Configure audio effects",
            "&7and volume settings",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.day-counter.material", "CLOCK");
        guiConfig.addDefault("gui.items.main-menu.day-counter.name", "&e📅 Day Counter");
        guiConfig.addDefault("gui.items.main-menu.day-counter.lore", Arrays.asList(
            "&7Manage day tracking and",
            "&7morning message system",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.statistics.material", "BOOK");
        guiConfig.addDefault("gui.items.main-menu.statistics.name", "&c📊 Statistics");
        guiConfig.addDefault("gui.items.main-menu.statistics.lore", Arrays.asList(
            "&7View detailed plugin",
            "&7usage statistics",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.world-management.material", "GRASS_BLOCK");
        guiConfig.addDefault("gui.items.main-menu.world-management.name", "&2🌍 World Management");
        guiConfig.addDefault("gui.items.main-menu.world-management.lore", Arrays.asList(
            "&7Manage world-specific",
            "&7sleep settings",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.player-management.material", "PLAYER_HEAD");
        guiConfig.addDefault("gui.items.main-menu.player-management.name", "&9👥 Player Management");
        guiConfig.addDefault("gui.items.main-menu.player-management.lore", Arrays.asList(
            "&7Configure player-specific",
            "&7settings and permissions",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.advanced-settings.material", "REDSTONE");
        guiConfig.addDefault("gui.items.main-menu.advanced-settings.name", "&4⚙️ Advanced Settings");
        guiConfig.addDefault("gui.items.main-menu.advanced-settings.lore", Arrays.asList(
            "&7Advanced configuration",
            "&7options for power users",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.particle-effects.material", "BLAZE_POWDER");
        guiConfig.addDefault("gui.items.main-menu.particle-effects.name", "&5🎆 Particle Effects");
        guiConfig.addDefault("gui.items.main-menu.particle-effects.lore", Arrays.asList(
            "&7Customize particle effects",
            "&7and visual enhancements",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.anti-spam-settings.material", "SHIELD");
        guiConfig.addDefault("gui.items.main-menu.anti-spam-settings.name", "&c🛡️ Anti-Spam Settings");
        guiConfig.addDefault("gui.items.main-menu.anti-spam-settings.lore", Arrays.asList(
            "&7Configure spam protection",
            "&7and cooldown settings",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.update-center.material", "COMPASS");
        guiConfig.addDefault("gui.items.main-menu.update-center.name", "&3🔄 Update Center");
        guiConfig.addDefault("gui.items.main-menu.update-center.lore", Arrays.asList(
            "&7Check for updates and",
            "&7plugin information",
            "",
            "&eClick to open!"
        ));
        
        guiConfig.addDefault("gui.items.main-menu.close.material", "BARRIER");
        guiConfig.addDefault("gui.items.main-menu.close.name", "&c❌ Close Menu");
        guiConfig.addDefault("gui.items.main-menu.close.lore", Arrays.asList(
            "&7Close the GUI menu",
            "",
            "&eClick to close!"
        ));
        
        // Sleep Settings Items
        guiConfig.addDefault("gui.items.sleep-settings.current-settings.material", "COMPASS");
        guiConfig.addDefault("gui.items.sleep-settings.current-settings.name", "&b📊 Current Settings");
        
        guiConfig.addDefault("gui.items.sleep-settings.percentage-1.material", "LIME_DYE");
        guiConfig.addDefault("gui.items.sleep-settings.percentage-10.material", "GREEN_DYE");
        guiConfig.addDefault("gui.items.sleep-settings.percentage-25.material", "YELLOW_DYE");
        guiConfig.addDefault("gui.items.sleep-settings.percentage-50.material", "ORANGE_DYE");
        guiConfig.addDefault("gui.items.sleep-settings.percentage-75.material", "RED_DYE");
        guiConfig.addDefault("gui.items.sleep-settings.percentage-100.material", "PURPLE_DYE");
        
        guiConfig.addDefault("gui.items.sleep-settings.custom-percentage.material", "WRITABLE_BOOK");
        guiConfig.addDefault("gui.items.sleep-settings.custom-percentage.name", "&e📝 Custom Percentage");
        guiConfig.addDefault("gui.items.sleep-settings.custom-percentage.lore", Arrays.asList(
            "&7Set a custom sleep percentage",
            "&7Type in chat after clicking",
            "",
            "&eClick to set custom value!"
        ));
        
        guiConfig.addDefault("gui.items.sleep-settings.reset-default.material", "STRUCTURE_VOID");
        guiConfig.addDefault("gui.items.sleep-settings.reset-default.name", "&c🔄 Reset to Default");
        guiConfig.addDefault("gui.items.sleep-settings.reset-default.lore", Arrays.asList(
            "&7Reset sleep percentage to 1%",
            "&7(Recommended for most servers)",
            "",
            "&eClick to reset!"
        ));
        
        guiConfig.addDefault("gui.items.sleep-settings.back.material", "ARROW");
        guiConfig.addDefault("gui.items.sleep-settings.back.name", "&7⬅ Back to Main Menu");
        guiConfig.addDefault("gui.items.sleep-settings.back.lore", Arrays.asList(
            "&7Return to the main menu",
            "",
            "&eClick to go back!"
        ));
        
        // Animation Settings Items
        guiConfig.addDefault("gui.items.animation-settings.intensity.material", "GLOWSTONE_DUST");
        guiConfig.addDefault("gui.items.animation-settings.intensity.name", "&e⚡ Animation Intensity");
        
        guiConfig.addDefault("gui.items.animation-settings.distance.material", "ENDER_EYE");
        guiConfig.addDefault("gui.items.animation-settings.distance.name", "&b👁 Animation Distance");
        
        guiConfig.addDefault("gui.items.animation-settings.duration.material", "CLOCK");
        guiConfig.addDefault("gui.items.animation-settings.duration.name", "&6⏰ Animation Duration");
        
        guiConfig.addDefault("gui.items.animation-settings.test.material", "FIREWORK_ROCKET");
        guiConfig.addDefault("gui.items.animation-settings.test.name", "&d🎆 Test Animation");
        guiConfig.addDefault("gui.items.animation-settings.test.lore", Arrays.asList(
            "&7Test the current animation",
            "&7settings on yourself",
            "",
            "&eClick to test!"
        ));
        
        guiConfig.addDefault("gui.items.animation-settings.back.material", "ARROW");
        guiConfig.addDefault("gui.items.animation-settings.back.name", "&7⬅ Back to Main Menu");
        guiConfig.addDefault("gui.items.animation-settings.back.lore", Arrays.asList(
            "&7Return to the main menu",
            "",
            "&eClick to go back!"
        ));
        
        // Sound Settings Items
        guiConfig.addDefault("gui.items.sound-settings.volume.material", "JUKEBOX");
        guiConfig.addDefault("gui.items.sound-settings.volume.name", "&a🔊 Master Volume");
        
        guiConfig.addDefault("gui.items.sound-settings.sleep-sound.material", "MUSIC_DISC_CAT");
        guiConfig.addDefault("gui.items.sound-settings.sleep-sound.name", "&b🎵 Sleep Sound");
        
        guiConfig.addDefault("gui.items.sound-settings.night-skip-sound.material", "MUSIC_DISC_BLOCKS");
        guiConfig.addDefault("gui.items.sound-settings.night-skip-sound.name", "&e🌙 Night Skip Sound");
        
        guiConfig.addDefault("gui.items.sound-settings.gui-sounds.material", "NOTE_BLOCK");
        guiConfig.addDefault("gui.items.sound-settings.gui-sounds.name", "&d🎹 GUI Sounds");
        guiConfig.addDefault("gui.items.sound-settings.gui-sounds.lore", Arrays.asList(
            "&7Enable or disable GUI",
            "&7click and navigation sounds",
            "",
            "&eClick to toggle!"
        ));
        
        guiConfig.addDefault("gui.items.sound-settings.test-sounds.material", "BELL");
        guiConfig.addDefault("gui.items.sound-settings.test-sounds.name", "&c🔔 Test All Sounds");
        guiConfig.addDefault("gui.items.sound-settings.test-sounds.lore", Arrays.asList(
            "&7Play all plugin sounds",
            "&7to test current settings",
            "",
            "&eClick to test!"
        ));
        
        guiConfig.addDefault("gui.items.sound-settings.back.material", "ARROW");
        guiConfig.addDefault("gui.items.sound-settings.back.name", "&7⬅ Back to Main Menu");
        guiConfig.addDefault("gui.items.sound-settings.back.lore", Arrays.asList(
            "&7Return to the main menu",
            "",
            "&eClick to go back!"
        ));
        
        // Day Counter Items
        guiConfig.addDefault("gui.items.day-counter.current-day.material", "SUNFLOWER");
        guiConfig.addDefault("gui.items.day-counter.current-day.name", "&e☀️ Current Day Information");
        
        guiConfig.addDefault("gui.items.day-counter.set-day-1.material", "WHITE_DYE");
        guiConfig.addDefault("gui.items.day-counter.set-day-10.material", "LIGHT_GRAY_DYE");
        guiConfig.addDefault("gui.items.day-counter.set-day-50.material", "GRAY_DYE");
        guiConfig.addDefault("gui.items.day-counter.set-day-100.material", "YELLOW_DYE");
        guiConfig.addDefault("gui.items.day-counter.set-day-365.material", "ORANGE_DYE");
        guiConfig.addDefault("gui.items.day-counter.set-day-1000.material", "RED_DYE");
        
        guiConfig.addDefault("gui.items.day-counter.custom-day.material", "WRITABLE_BOOK");
        guiConfig.addDefault("gui.items.day-counter.custom-day.name", "&e📝 Set Custom Day");
        guiConfig.addDefault("gui.items.day-counter.custom-day.lore", Arrays.asList(
            "&7Set a custom day number",
            "&7Type in chat after clicking",
            "",
            "&eClick to set custom day!"
        ));
        
        guiConfig.addDefault("gui.items.day-counter.reset-day.material", "STRUCTURE_VOID");
        guiConfig.addDefault("gui.items.day-counter.reset-day.name", "&c🔄 Reset to Day 1");
        guiConfig.addDefault("gui.items.day-counter.reset-day.lore", Arrays.asList(
            "&7Reset the day counter",
            "&7back to Day 1",
            "",
            "&eClick to reset!"
        ));
        
        guiConfig.addDefault("gui.items.day-counter.morning-messages.material", "PAPER");
        guiConfig.addDefault("gui.items.day-counter.morning-messages.name", "&a📜 Morning Messages");
        guiConfig.addDefault("gui.items.day-counter.morning-messages.lore", Arrays.asList(
            "&7Configure random morning",
            "&7messages for new days",
            "",
            "&eClick to configure!"
        ));
        
        guiConfig.addDefault("gui.items.day-counter.back.material", "ARROW");
        guiConfig.addDefault("gui.items.day-counter.back.name", "&7⬅ Back to Main Menu");
        guiConfig.addDefault("gui.items.day-counter.back.lore", Arrays.asList(
            "&7Return to the main menu",
            "",
            "&eClick to go back!"
        ));
        
        // Statistics Items
        guiConfig.addDefault("gui.items.statistics.sleep-events.material", "BED");
        guiConfig.addDefault("gui.items.statistics.sleep-events.name", "&b🛏️ Sleep Events");
        
        guiConfig.addDefault("gui.items.statistics.night-skips.material", "CLOCK");
        guiConfig.addDefault("gui.items.statistics.night-skips.name", "&e🌙 Night Skips");
        
        guiConfig.addDefault("gui.items.statistics.days-tracked.material", "CALENDAR");
        guiConfig.addDefault("gui.items.statistics.days-tracked.name", "&a📅 Days Tracked");
        
        guiConfig.addDefault("gui.items.statistics.players-served.material", "PLAYER_HEAD");
        guiConfig.addDefault("gui.items.statistics.players-served.name", "&d👥 Players Served");
        
        guiConfig.addDefault("gui.items.statistics.current-session.material", "EMERALD");
        guiConfig.addDefault("gui.items.statistics.current-session.name", "&2📊 Current Session");
        
        guiConfig.addDefault("gui.items.statistics.plugin-info.material", "BOOK");
        guiConfig.addDefault("gui.items.statistics.plugin-info.name", "&6📖 Plugin Information");
        
        guiConfig.addDefault("gui.items.statistics.export.material", "WRITABLE_BOOK");
        guiConfig.addDefault("gui.items.statistics.export.name", "&3📤 Export Statistics");
        guiConfig.addDefault("gui.items.statistics.export.lore", Arrays.asList(
            "&7Export statistics to a file",
            "&7for external analysis",
            "",
            "&eClick to export!"
        ));
        
        guiConfig.addDefault("gui.items.statistics.reset.material", "TNT");
        guiConfig.addDefault("gui.items.statistics.reset.name", "&c💥 Reset Statistics");
        guiConfig.addDefault("gui.items.statistics.reset.lore", Arrays.asList(
            "&7Reset all statistics",
            "&c&lWARNING: This cannot be undone!",
            "",
            "&eClick to reset!"
        ));
        
        guiConfig.addDefault("gui.items.statistics.back.material", "ARROW");
        guiConfig.addDefault("gui.items.statistics.back.name", "&7⬅ Back to Main Menu");
        guiConfig.addDefault("gui.items.statistics.back.lore", Arrays.asList(
            "&7Return to the main menu",
            "",
            "&eClick to go back!"
        ));
        
        // GUI Sounds
        guiConfig.addDefault("gui.sounds.enabled", true);
        guiConfig.addDefault("gui.sounds.volume", 0.5);
        guiConfig.addDefault("gui.sounds.GUI_OPEN", "UI_BUTTON_CLICK");
        guiConfig.addDefault("gui.sounds.GUI_CLOSE", "UI_BUTTON_CLICK");
        guiConfig.addDefault("gui.sounds.GUI_CLICK", "UI_BUTTON_CLICK");
        guiConfig.addDefault("gui.sounds.GUI_SUCCESS", "ENTITY_EXPERIENCE_ORB_PICKUP");
        guiConfig.addDefault("gui.sounds.GUI_ERROR", "ENTITY_VILLAGER_NO");
        guiConfig.addDefault("gui.sounds.GUI_BACK", "UI_BUTTON_CLICK");
        
        guiConfig.options().copyDefaults(true);
        saveConfig();
    }
    
    /**
     * Get GUI title
     */
    public String getGUITitle(String guiName) {
        return guiConfig.getString("gui.titles." + guiName, "&6&lEasySleep GUI");
    }
    
    /**
     * Get item material
     */
    public String getItemMaterial(String guiName, String itemName) {
        return guiConfig.getString("gui.items." + guiName + "." + itemName + ".material", "STONE");
    }
    
    /**
     * Get item name
     */
    public String getItemName(String guiName, String itemName) {
        return guiConfig.getString("gui.items." + guiName + "." + itemName + ".name", "&fItem");
    }
    
    /**
     * Get item lore
     */
    public List<String> getItemLore(String guiName, String itemName) {
        return guiConfig.getStringList("gui.items." + guiName + "." + itemName + ".lore");
    }
    
    /**
     * Check if GUI sounds are enabled
     */
    public boolean areGUISoundsEnabled() {
        return guiConfig.getBoolean("sounds.enabled", true);
    }
    
    /**
     * Get GUI sound volume
     */
    public double getGUISoundVolume() {
        return guiConfig.getDouble("sounds.volume", 0.5);
    }
    
    /**
     * Get GUI sound
     */
    public String getGUISound(String soundType) {
        return guiConfig.getString("sounds." + soundType, "UI_BUTTON_CLICK");
    }
    
    /**
     * Save configuration
     */
    private void saveConfig() {
        try {
            guiConfig.save(guiFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save gui.yml: " + e.getMessage());
        }
    }
    
    /**
     * Reload configuration
     */
    public void reloadConfig() {
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        loadDefaults();
    }
}