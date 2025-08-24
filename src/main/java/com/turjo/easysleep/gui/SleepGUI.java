package com.turjo.easysleep.gui;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advanced GUI System for EasySleep Plugin
 * Provides comprehensive interface for all plugin features
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class SleepGUI implements Listener {
    
    private final EasySleep plugin;
    private final Map<Player, String> playerMenus;
    private final Map<Player, Integer> playerPages;
    private final Map<Player, String> pendingInput;
    
    public SleepGUI(EasySleep plugin) {
        this.plugin = plugin;
        this.playerMenus = new HashMap<>();
        this.playerPages = new HashMap<>();
        this.pendingInput = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Open the main GUI menu
     */
    public void openMainMenu(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("main-menu")) {
            MessageUtils.sendMessage(player, "&cMain menu is currently disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("main-menu"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "MAIN_MENU");
        playerPages.put(player, 0);
        
        // Fill with glass panes for better appearance
        ItemStack glass = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        // Sleep Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("sleep-settings")) {
            gui.setItem(10, createGUIItem("main-menu", "sleep-settings"));
        }
        
        // Animation Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("animation-settings")) {
            gui.setItem(11, createGUIItem("main-menu", "animation-settings"));
        }
        
        // Sound Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("sound-settings")) {
            gui.setItem(12, createGUIItem("main-menu", "sound-settings"));
        }
        
        // Day Counter
        if (plugin.getSectionConfigManager().isSectionEnabled("day-counter")) {
            gui.setItem(13, createGUIItem("main-menu", "day-counter"));
        }
        
        // Statistics
        if (plugin.getSectionConfigManager().isSectionEnabled("statistics")) {
            gui.setItem(14, createGUIItem("main-menu", "statistics"));
        }
        
        // World Management
        if (plugin.getSectionConfigManager().isSectionEnabled("world-management")) {
            gui.setItem(15, createGUIItem("main-menu", "world-management"));
        }
        
        // Player Management
        if (plugin.getSectionConfigManager().isSectionEnabled("player-management")) {
            gui.setItem(19, createGUIItem("main-menu", "player-management"));
        }
        
        // Advanced Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("advanced-settings")) {
            gui.setItem(20, createGUIItem("main-menu", "advanced-settings"));
        }
        
        // Particle Effects
        if (plugin.getSectionConfigManager().isSectionEnabled("particle-effects")) {
            gui.setItem(21, createGUIItem("main-menu", "particle-effects"));
        }
        
        // Anti-Spam Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("anti-spam-settings")) {
            gui.setItem(22, createGUIItem("main-menu", "anti-spam-settings"));
        }
        
        // Update Center
        if (plugin.getSectionConfigManager().isSectionEnabled("update-center")) {
            gui.setItem(23, createGUIItem("main-menu", "update-center"));
        }
        
        // Close button
        gui.setItem(49, createGUIItem("main-menu", "close"));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Open Sleep Settings GUI
     */
    public void openSleepSettings(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("sleep-settings")) {
            MessageUtils.sendMessage(player, "&cSleep settings section is disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("sleep-settings"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "SLEEP_SETTINGS");
        playerPages.put(player, 0);
        
        // Fill with glass panes
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        World world = player.getWorld();
        Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int sleepingPlayers = (int) world.getPlayers().stream().mapToLong(p -> p.isSleeping() ? 1 : 0).sum();
        
        // Current Settings Display
        gui.setItem(4, createItem(
            Material.COMPASS,
            "&b📊 Current Sleep Settings",
            Arrays.asList(
                "&7World: &e" + world.getName(),
                "&7Sleep Percentage: &a" + (currentPercentage != null ? currentPercentage + "%" : "Unknown"),
                "&7Online Players: &b" + world.getPlayers().size(),
                "&7Sleeping Players: &d" + sleepingPlayers,
                "&7Required Players: &e" + (currentPercentage != null ? Math.ceil(currentPercentage * world.getPlayers().size() / 100.0) : "Unknown")
            )
        ));
        
        // Quick Set Options
        int[] percentages = {1, 10, 25, 50, 75, 100};
        Material[] materials = {Material.LIME_DYE, Material.GREEN_DYE, Material.YELLOW_DYE, 
                               Material.ORANGE_DYE, Material.RED_DYE, Material.PURPLE_DYE};
        int[] slots = {19, 20, 21, 22, 23, 24};
        
        for (int i = 0; i < percentages.length; i++) {
            boolean isActive = currentPercentage != null && currentPercentage == percentages[i];
            gui.setItem(slots[i], createItem(
                materials[i],
                (isActive ? "&a✓ " : "&7") + percentages[i] + "% Sleep Requirement",
                Arrays.asList(
                    "&7Click to set sleep percentage to &e" + percentages[i] + "%",
                    "&7Required players: &b" + Math.ceil(percentages[i] * world.getPlayers().size() / 100.0),
                    isActive ? "&a&l✓ Currently Active" : "",
                    "",
                    "&eClick to apply!"
                )
            ));
        }
        
        // Custom Percentage
        gui.setItem(31, createItem(
            Material.WRITABLE_BOOK,
            "&e📝 Custom Percentage",
            Arrays.asList(
                "&7Set a custom sleep percentage",
                "&7Type in chat after clicking",
                "&7Range: 0-100%",
                "",
                "&eClick to set custom value!"
            )
        ));
        
        // Reset to Default
        gui.setItem(40, createItem(
            Material.STRUCTURE_VOID,
            "&c🔄 Reset to Default",
            Arrays.asList(
                "&7Reset sleep percentage to 1%",
                "&7(Recommended for most servers)",
                "",
                "&eClick to reset!"
            )
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.ARROW,
            "&7⬅ Back to Main Menu",
            Arrays.asList(
                "&7Return to the main menu",
                "",
                "&eClick to go back!"
            )
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Open Animation Settings GUI
     */
    public void openAnimationSettings(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("animation-settings")) {
            MessageUtils.sendMessage(player, "&cAnimation settings section is disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("animation-settings"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "ANIMATION_SETTINGS");
        playerPages.put(player, 0);
        
        // Fill with glass panes
        ItemStack glass = createItem(Material.PURPLE_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        // Toggle Animations
        gui.setItem(10, createItem(
            plugin.getConfigManager().areAnimationsEnabled() ? Material.LIME_DYE : Material.GRAY_DYE,
            "&aToggle Animations",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().areAnimationsEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Enable or disable all animations",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Animation Intensity
        int intensity = plugin.getConfigManager().getAnimationIntensity();
        gui.setItem(11, createItem(
            Material.GLOWSTONE_DUST,
            "&e⚡ Animation Intensity",
            Arrays.asList(
                "&7Current Intensity: &e" + intensity + "/3",
                "&7Range: 1-3 (Higher = More particles)",
                "&7Level 1: &7Minimal particles",
                "&7Level 2: &eModerate particles",
                "&7Level 3: &cIntense particles",
                "",
                "&eClick to cycle through levels!"
            )
        ));
        
        // Enhanced Particles
        gui.setItem(12, createItem(
            plugin.getConfigManager().areEnhancedParticlesEnabled() ? Material.FIREWORK_STAR : Material.GUNPOWDER,
            "&bEnhanced Particles",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().areEnhancedParticlesEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Extra particle effects for better visuals",
                "&7Adds more detailed particle effects",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Clock Animation
        gui.setItem(13, createItem(
            plugin.getConfigManager().isClockAnimationEnabled() ? Material.CLOCK : Material.GRAY_DYE,
            "&6Clock Animation",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().isClockAnimationEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Shows floating clocks above sleeping players",
                "&7Displays real game time",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Day-Night Animation
        gui.setItem(14, createItem(
            plugin.getConfigManager().isDayNightAnimationEnabled() ? Material.GOLDEN_APPLE : Material.APPLE,
            "&eDay-Night Cycle Animation",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().isDayNightAnimationEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Epic time acceleration effects",
                "&7Visual time speed-up animation",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Animation Distance
        gui.setItem(19, createItem(
            Material.ENDER_EYE,
            "&b👁 Animation Distance",
            Arrays.asList(
                "&7Current Distance: &e" + plugin.getConfigManager().getMaxAnimationDistance() + " blocks",
                "&7Maximum distance for animation rendering",
                "&7Higher values = More visible but more lag",
                "&7Click to cycle: 8, 16, 32, 64 blocks",
                "",
                "&eClick to change!"
            )
        ));
        
        // Performance Mode
        gui.setItem(20, createItem(
            plugin.getConfigManager().isPerformanceMode() ? Material.REDSTONE : Material.GLOWSTONE_DUST,
            "&cPerformance Mode",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().isPerformanceMode() ? "&aEnabled" : "&cDisabled"),
                "&7Reduces particles for better performance",
                "&7Recommended for servers with many players",
                "&7Especially helpful for Bedrock players",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Animation Duration
        gui.setItem(21, createItem(
            Material.CLOCK,
            "&6⏰ Animation Duration",
            Arrays.asList(
                "&7Current Duration: &e" + plugin.getConfigManager().getAnimationDuration() + " seconds",
                "&7How long animations last",
                "&7Range: 1-10 seconds",
                "&7Click to cycle through durations",
                "",
                "&eClick to change!"
            )
        ));
        
        // Particle Density
        gui.setItem(22, createItem(
            Material.BLAZE_POWDER,
            "&5🎆 Particle Density",
            Arrays.asList(
                "&7Current Density: &e" + String.format("%.1f", plugin.getConfigManager().getParticleDensity()) + "x",
                "&7Multiplier for particle count",
                "&7Range: 0.1x - 3.0x",
                "&7Lower = Less lag, Higher = More visual",
                "",
                "&eClick to cycle through densities!"
            )
        ));
        
        // Test Animation
        gui.setItem(31, createItem(
            Material.FIREWORK_ROCKET,
            "&d🎆 Test Animation",
            Arrays.asList(
                "&7Test the current animation",
                "&7settings on yourself",
                "&7Shows a preview of sleep animations",
                "",
                "&eClick to test!"
            )
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.ARROW,
            "&7⬅ Back to Main Menu",
            Arrays.asList(
                "&7Return to the main menu",
                "",
                "&eClick to go back!"
            )
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Open Sound Settings GUI
     */
    public void openSoundSettings(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("sound-settings")) {
            MessageUtils.sendMessage(player, "&cSound settings section is disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("sound-settings"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "SOUND_SETTINGS");
        playerPages.put(player, 0);
        
        // Fill with glass panes
        ItemStack glass = createItem(Material.GREEN_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        // Toggle Sound Effects
        gui.setItem(10, createItem(
            plugin.getConfigManager().areSoundEffectsEnabled() ? Material.NOTE_BLOCK : Material.GRAY_DYE,
            "&aSounds Enabled",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().areSoundEffectsEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Enable or disable all sound effects",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Sound Volume
        double volume = plugin.getConfigManager().getSoundVolume();
        gui.setItem(11, createItem(
            Material.JUKEBOX,
            "&a🔊 Master Volume",
            Arrays.asList(
                "&7Current Volume: &e" + (int)(volume * 100) + "%",
                "&7Master volume for all sounds",
                "&7Range: 10% - 200%",
                "&7Click to cycle through volumes",
                "",
                "&eClick to change!"
            )
        ));
        
        // Sleep Sound
        gui.setItem(19, createItem(
            Material.MUSIC_DISC_CAT,
            "&b🎵 Sleep Sound",
            Arrays.asList(
                "&7Current: &e" + plugin.getConfigManager().getSleepSound(),
                "&7Sound played when players sleep",
                "&7Gentle, relaxing sounds",
                "",
                "&eLeft-click to change!",
                "&7Right-click to test!"
            )
        ));
        
        // Night Skip Sound
        gui.setItem(20, createItem(
            Material.MUSIC_DISC_BLOCKS,
            "&e🌙 Night Skip Sound",
            Arrays.asList(
                "&7Current: &e" + plugin.getConfigManager().getNightSkipSound(),
                "&7Sound played during night skip",
                "&7Epic completion sounds",
                "",
                "&eLeft-click to change!",
                "&7Right-click to test!"
            )
        ));
        
        // GUI Sounds
        gui.setItem(21, createItem(
            plugin.getGUIConfigManager().areGUISoundsEnabled() ? Material.NOTE_BLOCK : Material.GRAY_DYE,
            "&d🎹 GUI Sounds",
            Arrays.asList(
                "&7Status: " + (plugin.getGUIConfigManager().areGUISoundsEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Enable or disable GUI",
                "&7click and navigation sounds",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Sound Test Area
        gui.setItem(31, createItem(
            Material.BELL,
            "&c🔔 Test All Sounds",
            Arrays.asList(
                "&7Play all plugin sounds",
                "&7to test current settings",
                "&7Plays sounds in sequence",
                "",
                "&eClick to test!"
            )
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.ARROW,
            "&7⬅ Back to Main Menu",
            Arrays.asList(
                "&7Return to the main menu",
                "",
                "&eClick to go back!"
            )
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Open Day Counter GUI
     */
    public void openDayCounter(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("day-counter")) {
            MessageUtils.sendMessage(player, "&cDay counter section is disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("day-counter"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "DAY_COUNTER");
        playerPages.put(player, 0);
        
        // Fill with glass panes
        ItemStack glass = createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        World world = player.getWorld();
        long currentDay = plugin.getDayCounterManager().getCurrentDay(world);
        
        // Current Day Display
        gui.setItem(4, createItem(
            Material.SUNFLOWER,
            "&e☀️ Current Day Information",
            Arrays.asList(
                "&7World: &e" + world.getName(),
                "&7Current Day: &a" + currentDay,
                "&7Time: &b" + getTimeString(world.getTime()),
                "&7Status: " + (world.getTime() > 12000 ? "&cNight" : "&aDay"),
                "&7Weather: " + (world.hasStorm() ? "&9Stormy" : "&aClear")
            )
        ));
        
        // Toggle Day Counter
        gui.setItem(10, createItem(
            plugin.getConfigManager().isDayCounterEnabled() ? Material.LIME_DYE : Material.GRAY_DYE,
            "&aDay Counter Enabled",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().isDayCounterEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Enable or disable day counting",
                "&7Shows day titles to players",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Set Day Options
        int[] days = {1, 10, 50, 100, 365, 1000};
        Material[] materials = {Material.WHITE_DYE, Material.LIGHT_GRAY_DYE, Material.GRAY_DYE, 
                               Material.YELLOW_DYE, Material.ORANGE_DYE, Material.RED_DYE};
        int[] slots = {19, 20, 21, 22, 23, 24};
        
        for (int i = 0; i < days.length; i++) {
            boolean isActive = currentDay == days[i];
            gui.setItem(slots[i], createItem(
                materials[i],
                (isActive ? "&a✓ " : "&7") + "Set to Day " + days[i],
                Arrays.asList(
                    "&7Click to set current day to &e" + days[i],
                    "&7This will update the day counter",
                    isActive ? "&a&l✓ Currently Active" : "",
                    "",
                    "&eClick to apply!"
                )
            ));
        }
        
        // Custom Day
        gui.setItem(31, createItem(
            Material.WRITABLE_BOOK,
            "&e📝 Set Custom Day",
            Arrays.asList(
                "&7Set a custom day number",
                "&7Type in chat after clicking",
                "&7Range: 1 - 999999",
                "",
                "&eClick to set custom day!"
            )
        ));
        
        // Reset Day
        gui.setItem(40, createItem(
            Material.STRUCTURE_VOID,
            "&c🔄 Reset to Day 1",
            Arrays.asList(
                "&7Reset the day counter",
                "&7back to Day 1",
                "&7Fresh start for your world",
                "",
                "&eClick to reset!"
            )
        ));
        
        // Morning Messages
        gui.setItem(41, createItem(
            Material.PAPER,
            "&a📜 Morning Messages",
            Arrays.asList(
                "&7Configure random morning",
                "&7messages for new days",
                "&7Currently: &e" + plugin.getConfigManager().getRandomMorningMessages().size() + " messages",
                "",
                "&eClick to configure!"
            )
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.ARROW,
            "&7⬅ Back to Main Menu",
            Arrays.asList(
                "&7Return to the main menu",
                "",
                "&eClick to go back!"
            )
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Open Statistics GUI
     */
    public void openStatistics(Player player) {
        if (!plugin.getSectionConfigManager().isSectionEnabled("statistics")) {
            MessageUtils.sendMessage(player, "&cStatistics section is disabled!");
            return;
        }
        
        String title = MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("statistics"));
        Inventory gui = Bukkit.createInventory(null, 54, title);
        
        playerMenus.put(player, "STATISTICS");
        playerPages.put(player, 0);
        
        // Fill with glass panes
        ItemStack glass = createItem(Material.RED_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            gui.setItem(i, glass);
        }
        
        // Sleep Events
        gui.setItem(10, createItem(
            Material.RED_BED,
            "&b🛏️ Sleep Events",
            Arrays.asList(
                "&7Total Sleep Events: &a" + plugin.getStatisticsManager().getTotalSleepEvents(),
                "&7Players entering beds",
                "&7Tracks every sleep attempt",
                "",
                "&7This tracks every time a player",
                "&7enters a bed to sleep"
            )
        ));
        
        // Night Skips
        gui.setItem(11, createItem(
            Material.CLOCK,
            "&e🌙 Night Skips",
            Arrays.asList(
                "&7Total Night Skips: &b" + plugin.getStatisticsManager().getTotalNightSkips(),
                "&7Successful night skips",
                "&7Complete sleep cycles",
                "",
                "&7This tracks every time the night",
                "&7was successfully skipped"
            )
        ));
        
        // Days Tracked
        gui.setItem(12, createItem(
            Material.PAPER,
            "&a📅 Days Tracked",
            Arrays.asList(
                "&7Total Days Tracked: &e" + plugin.getStatisticsManager().getTotalDaysTracked(),
                "&7Days counted by the plugin",
                "&7Across all worlds",
                "",
                "&7This shows how many days have",
                "&7passed since plugin installation"
            )
        ));
        
        // Players Served
        gui.setItem(13, createItem(
            Material.PLAYER_HEAD,
            "&d👥 Players Served",
            Arrays.asList(
                "&7Players Served: &d" + plugin.getStatisticsManager().getTotalPlayersServed(),
                "&7Maximum concurrent players",
                "&7Peak server population",
                "",
                "&7This shows the highest number of",
                "&7players online at the same time"
            )
        ));
        
        // Current Session
        gui.setItem(19, createItem(
            Material.EMERALD,
            "&2📊 Current Session",
            Arrays.asList(
                "&7Current Online: &a" + Bukkit.getOnlinePlayers().size(),
                "&7AFK Players: &c" + plugin.getAFKManager().getAFKCount(),
                "&7Active Players: &b" + (Bukkit.getOnlinePlayers().size() - plugin.getAFKManager().getAFKCount()),
                "&7Worlds Loaded: &e" + Bukkit.getWorlds().size(),
                "",
                "&7Real-time server statistics"
            )
        ));
        
        // Plugin Info
        gui.setItem(22, createItem(
            Material.BOOK,
            "&6📖 Plugin Information",
            Arrays.asList(
                "&7Plugin Version: &e" + plugin.getDescription().getVersion(),
                "&7Author: &a" + plugin.getDescription().getAuthors().get(0),
                "&7API Version: &b" + plugin.getDescription().getAPIVersion(),
                "&7Server Version: &d" + Bukkit.getVersion(),
                "",
                "&7EasySleep - Ultimate Sleep Management"
            )
        ));
        
        // Export Statistics
        gui.setItem(40, createItem(
            Material.WRITABLE_BOOK,
            "&3📤 Export Statistics",
            Arrays.asList(
                "&7Export statistics to a file",
                "&7for external analysis",
                "&7Creates a detailed report",
                "",
                "&eClick to export!"
            )
        ));
        
        // Reset Statistics
        gui.setItem(41, createItem(
            Material.TNT,
            "&c💥 Reset Statistics",
            Arrays.asList(
                "&7Reset all statistics",
                "&c&lWARNING: This cannot be undone!",
                "&7This will clear all tracked data",
                "",
                "&eClick to reset!"
            )
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.ARROW,
            "&7⬅ Back to Main Menu",
            Arrays.asList(
                "&7Return to the main menu",
                "",
                "&eClick to go back!"
            )
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Check if player has pending input
     */
    public boolean hasPendingInput(Player player) {
        return pendingInput.containsKey(player);
    }
    
    /**
     * Handle chat input for GUI
     */
    public void handleChatInput(Player player, String input) {
        String inputType = pendingInput.remove(player);
        if (inputType == null) return;
        
        try {
            switch (inputType) {
                case "CUSTOM_PERCENTAGE":
                    int percentage = Integer.parseInt(input);
                    if (percentage < 0 || percentage > 100) {
                        MessageUtils.sendMessage(player, "&cInvalid percentage! Must be between 0-100.");
                        return;
                    }
                    player.getWorld().setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentage);
                    MessageUtils.sendMessage(player, "&a✓ Set sleep percentage to &e" + percentage + "%");
                    openSleepSettings(player);
                    break;
                    
                case "CUSTOM_DAY":
                    long day = Long.parseLong(input);
                    if (day < 1 || day > 999999) {
                        MessageUtils.sendMessage(player, "&cInvalid day! Must be between 1-999999.");
                        return;
                    }
                    plugin.getDayCounterManager().setDay(player.getWorld(), day);
                    MessageUtils.sendMessage(player, "&a✓ Set day counter to &eDay " + day);
                    openDayCounter(player);
                    break;
            }
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(player, "&cInvalid number format! Please enter a valid number.");
        }
    }
    
    /**
     * Create GUI item from config
     */
    private ItemStack createGUIItem(String guiName, String itemName) {
        try {
            Material material = Material.valueOf(plugin.getGUIConfigManager().getItemMaterial(guiName, itemName));
            String name = plugin.getGUIConfigManager().getItemName(guiName, itemName);
            List<String> lore = plugin.getGUIConfigManager().getItemLore(guiName, itemName);
            return createItem(material, name, lore);
        } catch (Exception e) {
            // Fallback to stone if material is invalid
            return createItem(Material.STONE, "&cInvalid Item", Arrays.asList("&7Configuration error"));
        }
    }
    
    /**
     * Create an item with name and lore
     */
    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.colorize(name));
            if (lore != null && !lore.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    if (line != null && !line.trim().isEmpty()) {
                        coloredLore.add(MessageUtils.colorize(line));
                    }
                }
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    /**
     * Get time string from world time
     */
    private String getTimeString(long time) {
        if (time >= 0 && time < 6000) {
            return "Morning";
        } else if (time >= 6000 && time < 12000) {
            return "Day";
        } else if (time >= 12000 && time < 18000) {
            return "Evening";
        } else {
            return "Night";
        }
    }
    
    /**
     * Play GUI sound
     */
    private void playSound(Player player, String soundType) {
        if (plugin.getGUIConfigManager().areGUISoundsEnabled()) {
            try {
                Sound sound = Sound.valueOf(plugin.getGUIConfigManager().getGUISound(soundType));
                float volume = (float) plugin.getGUIConfigManager().getGUISoundVolume();
                player.playSound(player.getLocation(), sound, volume, 1.0f);
            } catch (Exception e) {
                // Fallback sound
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String menuType = playerMenus.get(player);
        
        if (menuType == null) return;
        
        event.setCancelled(true);
        
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        
        int slot = event.getSlot();
        
        // Handle different menu types
        switch (menuType) {
            case "MAIN_MENU":
                handleMainMenuClick(player, slot);
                break;
            case "SLEEP_SETTINGS":
                handleSleepSettingsClick(player, slot);
                break;
            case "ANIMATION_SETTINGS":
                handleAnimationSettingsClick(player, slot);
                break;
            case "SOUND_SETTINGS":
                handleSoundSettingsClick(player, slot);
                break;
            case "DAY_COUNTER":
                handleDayCounterClick(player, slot);
                break;
            case "STATISTICS":
                handleStatisticsClick(player, slot);
                break;
        }
    }
    
    /**
     * Handle main menu clicks
     */
    private void handleMainMenuClick(Player player, int slot) {
        playSound(player, "GUI_CLICK");
        
        switch (slot) {
            case 10: // Sleep Settings
                if (plugin.getSectionConfigManager().isSectionEnabled("sleep-settings")) {
                    openSleepSettings(player);
                }
                break;
            case 11: // Animation Settings
                if (plugin.getSectionConfigManager().isSectionEnabled("animation-settings")) {
                    openAnimationSettings(player);
                }
                break;
            case 12: // Sound Settings
                if (plugin.getSectionConfigManager().isSectionEnabled("sound-settings")) {
                    openSoundSettings(player);
                }
                break;
            case 13: // Day Counter
                if (plugin.getSectionConfigManager().isSectionEnabled("day-counter")) {
                    openDayCounter(player);
                }
                break;
            case 14: // Statistics
                if (plugin.getSectionConfigManager().isSectionEnabled("statistics")) {
                    openStatistics(player);
                }
                break;
            case 49: // Close
                player.closeInventory();
                playSound(player, "GUI_CLOSE");
                break;
        }
    }
    
    /**
     * Handle sleep settings clicks
     */
    private void handleSleepSettingsClick(Player player, int slot) {
        World world = player.getWorld();
        
        // Percentage buttons
        if (slot >= 19 && slot <= 24) {
            int[] percentages = {1, 10, 25, 50, 75, 100};
            int index = slot - 19;
            if (index < percentages.length) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentages[index]);
                MessageUtils.sendMessage(player, "&a✓ Set sleep percentage to &e" + percentages[index] + "%");
                playSound(player, "GUI_SUCCESS");
                openSleepSettings(player); // Refresh GUI
            }
        }
        
        switch (slot) {
            case 31: // Custom Percentage
                player.closeInventory();
                pendingInput.put(player, "CUSTOM_PERCENTAGE");
                MessageUtils.sendMessage(player, "&e📝 Enter custom sleep percentage (0-100) in chat:");
                break;
            case 40: // Reset to Default
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 1);
                MessageUtils.sendMessage(player, "&a✓ Reset sleep percentage to default (1%)");
                playSound(player, "GUI_SUCCESS");
                openSleepSettings(player);
                break;
            case 45: // Back
                openMainMenu(player);
                playSound(player, "GUI_BACK");
                break;
        }
    }
    
    /**
     * Handle animation settings clicks
     */
    private void handleAnimationSettingsClick(Player player, int slot) {
        switch (slot) {
            case 10: // Toggle Animations
                toggleConfigValue("settings.enable-animations");
                MessageUtils.sendMessage(player, "&a✓ Animations " + (plugin.getConfigManager().areAnimationsEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 11: // Animation Intensity
                cycleIntensity();
                MessageUtils.sendMessage(player, "&a✓ Animation intensity set to " + plugin.getConfigManager().getAnimationIntensity());
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 12: // Enhanced Particles
                toggleConfigValue("features.enhanced-particles");
                MessageUtils.sendMessage(player, "&a✓ Enhanced particles " + (plugin.getConfigManager().areEnhancedParticlesEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 13: // Clock Animation
                toggleConfigValue("features.clock-animation");
                MessageUtils.sendMessage(player, "&a✓ Clock animation " + (plugin.getConfigManager().isClockAnimationEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 14: // Day-Night Animation
                toggleConfigValue("features.day-night-animation");
                MessageUtils.sendMessage(player, "&a✓ Day-night animation " + (plugin.getConfigManager().isDayNightAnimationEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 19: // Animation Distance
                cycleAnimationDistance();
                MessageUtils.sendMessage(player, "&a✓ Animation distance set to " + plugin.getConfigManager().getMaxAnimationDistance() + " blocks");
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 20: // Performance Mode
                toggleConfigValue("advanced.performance-mode");
                MessageUtils.sendMessage(player, "&a✓ Performance mode " + (plugin.getConfigManager().isPerformanceMode() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 21: // Animation Duration
                cycleAnimationDuration();
                MessageUtils.sendMessage(player, "&a✓ Animation duration set to " + plugin.getConfigManager().getAnimationDuration() + " seconds");
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 22: // Particle Density
                cycleParticleDensity();
                MessageUtils.sendMessage(player, "&a✓ Particle density set to " + String.format("%.1f", plugin.getConfigManager().getParticleDensity()) + "x");
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
                break;
            case 31: // Test Animation
                MessageUtils.sendMessage(player, "&d🎆 Testing animation effects...");
                plugin.getAnimationManager().startSleepAnimation(player);
                playSound(player, "GUI_SUCCESS");
                break;
            case 45: // Back
                openMainMenu(player);
                playSound(player, "GUI_BACK");
                break;
        }
    }
    
    /**
     * Handle sound settings clicks
     */
    private void handleSoundSettingsClick(Player player, int slot) {
        switch (slot) {
            case 10: // Toggle Sound Effects
                toggleConfigValue("settings.enable-sound-effects");
                MessageUtils.sendMessage(player, "&a✓ Sound effects " + (plugin.getConfigManager().areSoundEffectsEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openSoundSettings(player);
                break;
            case 11: // Sound Volume
                cycleSoundVolume();
                MessageUtils.sendMessage(player, "&a✓ Sound volume set to " + (int)(plugin.getConfigManager().getSoundVolume() * 100) + "%");
                playSound(player, "GUI_SUCCESS");
                openSoundSettings(player);
                break;
            case 31: // Test All Sounds
                MessageUtils.sendMessage(player, "&c🔔 Testing all sounds...");
                testAllSounds(player);
                playSound(player, "GUI_SUCCESS");
                break;
            case 45: // Back
                openMainMenu(player);
                playSound(player, "GUI_BACK");
                break;
        }
    }
    
    /**
     * Handle day counter clicks
     */
    private void handleDayCounterClick(Player player, int slot) {
        World world = player.getWorld();
        
        // Day setting buttons
        if (slot >= 19 && slot <= 24) {
            int[] days = {1, 10, 50, 100, 365, 1000};
            int index = slot - 19;
            if (index < days.length) {
                plugin.getDayCounterManager().setDay(world, days[index]);
                MessageUtils.sendMessage(player, "&a✓ Set day counter to &eDay " + days[index]);
                playSound(player, "GUI_SUCCESS");
                openDayCounter(player);
            }
        }
        
        switch (slot) {
            case 10: // Toggle Day Counter
                toggleConfigValue("features.day-counter");
                MessageUtils.sendMessage(player, "&a✓ Day counter " + (plugin.getConfigManager().isDayCounterEnabled() ? "enabled" : "disabled"));
                playSound(player, "GUI_SUCCESS");
                openDayCounter(player);
                break;
            case 31: // Custom Day
                player.closeInventory();
                pendingInput.put(player, "CUSTOM_DAY");
                MessageUtils.sendMessage(player, "&e📝 Enter custom day number (1-999999) in chat:");
                break;
            case 40: // Reset Day
                plugin.getDayCounterManager().resetDay(world);
                MessageUtils.sendMessage(player, "&a✓ Reset day counter to Day 1");
                playSound(player, "GUI_SUCCESS");
                openDayCounter(player);
                break;
            case 45: // Back
                openMainMenu(player);
                playSound(player, "GUI_BACK");
                break;
        }
    }
    
    /**
     * Handle statistics clicks
     */
    private void handleStatisticsClick(Player player, int slot) {
        switch (slot) {
            case 45: // Back
                openMainMenu(player);
                playSound(player, "GUI_BACK");
                break;
        }
    }
    
    /**
     * Toggle a boolean config value
     */
    private void toggleConfigValue(String path) {
        boolean currentValue = plugin.getConfig().getBoolean(path);
        plugin.getConfig().set(path, !currentValue);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Cycle through animation intensity levels
     */
    private void cycleIntensity() {
        int current = plugin.getConfigManager().getAnimationIntensity();
        int next = current >= 3 ? 1 : current + 1;
        plugin.getConfig().set("advanced.animation-intensity", next);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Cycle through animation distances
     */
    private void cycleAnimationDistance() {
        int current = plugin.getConfigManager().getMaxAnimationDistance();
        int next;
        switch (current) {
            case 8: next = 16; break;
            case 16: next = 32; break;
            case 32: next = 64; break;
            default: next = 8; break;
        }
        plugin.getConfig().set("advanced.max-animation-distance", next);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Cycle through animation durations
     */
    private void cycleAnimationDuration() {
        int current = plugin.getConfigManager().getAnimationDuration();
        int next = current >= 10 ? 1 : current + 1;
        plugin.getConfig().set("advanced.animation-duration", next);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Cycle through particle densities
     */
    private void cycleParticleDensity() {
        double current = plugin.getConfigManager().getParticleDensity();
        double next;
        if (current <= 0.1) next = 0.5;
        else if (current <= 0.5) next = 1.0;
        else if (current <= 1.0) next = 1.5;
        else if (current <= 1.5) next = 2.0;
        else if (current <= 2.0) next = 3.0;
        else next = 0.1;
        
        plugin.getConfig().set("effects.particle-density", next);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Cycle through sound volumes
     */
    private void cycleSoundVolume() {
        double current = plugin.getConfigManager().getSoundVolume();
        double next;
        if (current <= 0.1) next = 0.25;
        else if (current <= 0.25) next = 0.5;
        else if (current <= 0.5) next = 0.75;
        else if (current <= 0.75) next = 1.0;
        else if (current <= 1.0) next = 1.5;
        else if (current <= 1.5) next = 2.0;
        else next = 0.1;
        
        plugin.getConfig().set("advanced.sound-volume", next);
        plugin.saveConfig();
        plugin.getConfigManager().reloadConfig();
    }
    
    /**
     * Test all sounds for the player
     */
    private void testAllSounds(Player player) {
        new BukkitRunnable() {
            int soundIndex = 0;
            String[] sounds = {
                plugin.getConfigManager().getSleepSound(),
                plugin.getConfigManager().getNightSkipSound(),
                "UI_BUTTON_CLICK",
                "ENTITY_EXPERIENCE_ORB_PICKUP"
            };
            String[] soundNames = {"Sleep Sound", "Night Skip Sound", "GUI Click", "Success Sound"};
            
            @Override
            public void run() {
                if (soundIndex >= sounds.length) {
                    MessageUtils.sendMessage(player, "&a✓ Sound test complete!");
                    cancel();
                    return;
                }
                
                try {
                    Sound sound = Sound.valueOf(sounds[soundIndex]);
                    float volume = (float) plugin.getConfigManager().getSoundVolume();
                    player.playSound(player.getLocation(), sound, volume, 1.0f);
                    MessageUtils.sendMessage(player, "&7Playing: &e" + soundNames[soundIndex]);
                } catch (Exception e) {
                    MessageUtils.sendMessage(player, "&cInvalid sound: &e" + sounds[soundIndex]);
                }
                
                soundIndex++;
            }
        }.runTaskTimer(plugin, 0L, 40L); // Play every 2 seconds
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            playerMenus.remove(player);
            playerPages.remove(player);
        }
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        playerMenus.clear();
        playerPages.clear();
        pendingInput.clear();
    }
}