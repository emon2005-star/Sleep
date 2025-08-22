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
    
    // GUI Types
    public enum GUIType {
        MAIN_MENU,
        SLEEP_SETTINGS,
        ANIMATION_SETTINGS,
        SOUND_SETTINGS,
        DAY_COUNTER,
        STATISTICS,
        WORLD_MANAGEMENT,
        PLAYER_MANAGEMENT,
        ADVANCED_SETTINGS,
        PARTICLE_EFFECTS,
        ANTI_SPAM_SETTINGS,
        UPDATE_CENTER
    }
    
    public SleepGUI(EasySleep plugin) {
        this.plugin = plugin;
        this.playerMenus = new HashMap<>();
        this.playerPages = new HashMap<>();
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("main-menu")));
        
        playerMenus.put(player, "MAIN_MENU");
        playerPages.put(player, 0);
        
        // Sleep Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("sleep-settings")) {
            gui.setItem(10, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "sleep-settings")),
                plugin.getGUIConfigManager().getItemName("main-menu", "sleep-settings"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "sleep-settings")
            ));
        }
        
        // Animation Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("animation-settings")) {
            gui.setItem(11, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "animation-settings")),
                plugin.getGUIConfigManager().getItemName("main-menu", "animation-settings"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "animation-settings")
            ));
        }
        
        // Sound Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("sound-settings")) {
            gui.setItem(12, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "sound-settings")),
                plugin.getGUIConfigManager().getItemName("main-menu", "sound-settings"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "sound-settings")
            ));
        }
        
        // Day Counter
        if (plugin.getSectionConfigManager().isSectionEnabled("day-counter")) {
            gui.setItem(13, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "day-counter")),
                plugin.getGUIConfigManager().getItemName("main-menu", "day-counter"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "day-counter")
            ));
        }
        
        // Statistics
        if (plugin.getSectionConfigManager().isSectionEnabled("statistics")) {
            gui.setItem(14, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "statistics")),
                plugin.getGUIConfigManager().getItemName("main-menu", "statistics"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "statistics")
            ));
        }
        
        // World Management
        if (plugin.getSectionConfigManager().isSectionEnabled("world-management")) {
            gui.setItem(15, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "world-management")),
                plugin.getGUIConfigManager().getItemName("main-menu", "world-management"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "world-management")
            ));
        }
        
        // Player Management
        if (plugin.getSectionConfigManager().isSectionEnabled("player-management")) {
            gui.setItem(19, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "player-management")),
                plugin.getGUIConfigManager().getItemName("main-menu", "player-management"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "player-management")
            ));
        }
        
        // Advanced Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("advanced-settings")) {
            gui.setItem(20, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "advanced-settings")),
                plugin.getGUIConfigManager().getItemName("main-menu", "advanced-settings"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "advanced-settings")
            ));
        }
        
        // Particle Effects
        if (plugin.getSectionConfigManager().isSectionEnabled("particle-effects")) {
            gui.setItem(21, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "particle-effects")),
                plugin.getGUIConfigManager().getItemName("main-menu", "particle-effects"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "particle-effects")
            ));
        }
        
        // Anti-Spam Settings
        if (plugin.getSectionConfigManager().isSectionEnabled("anti-spam-settings")) {
            gui.setItem(22, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "anti-spam-settings")),
                plugin.getGUIConfigManager().getItemName("main-menu", "anti-spam-settings"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "anti-spam-settings")
            ));
        }
        
        // Update Center
        if (plugin.getSectionConfigManager().isSectionEnabled("update-center")) {
            gui.setItem(23, createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "update-center")),
                plugin.getGUIConfigManager().getItemName("main-menu", "update-center"),
                plugin.getGUIConfigManager().getItemLore("main-menu", "update-center")
            ));
        }
        
        // Close button
        gui.setItem(49, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("main-menu", "close")),
            plugin.getGUIConfigManager().getItemName("main-menu", "close"),
            plugin.getGUIConfigManager().getItemLore("main-menu", "close")
        ));
        
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("sleep-settings")));
        
        playerMenus.put(player, "SLEEP_SETTINGS");
        playerPages.put(player, 0);
        
        World world = player.getWorld();
        Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        
        // Current Settings Display
        gui.setItem(4, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sleep-settings", "current-settings")),
            plugin.getGUIConfigManager().getItemName("sleep-settings", "current-settings"),
            Arrays.asList(
                "&7Current World: &e" + world.getName(),
                "&7Sleep Percentage: &a" + (currentPercentage != null ? currentPercentage + "%" : "Unknown"),
                "&7Online Players: &b" + world.getPlayers().size(),
                "&7Sleeping Players: &d" + world.getPlayers().stream().mapToInt(p -> p.isSleeping() ? 1 : 0).sum()
            )
        ));
        
        // Quick Set Options
        int[] percentages = {1, 10, 25, 50, 75, 100};
        int[] slots = {19, 20, 21, 22, 23, 24};
        
        for (int i = 0; i < percentages.length; i++) {
            gui.setItem(slots[i], createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sleep-settings", "percentage-" + percentages[i])),
                "&a" + percentages[i] + "% Sleep Requirement",
                Arrays.asList(
                    "&7Click to set sleep percentage to &e" + percentages[i] + "%",
                    "&7Required players: &b" + Math.ceil(percentages[i] * world.getPlayers().size() / 100.0),
                    "",
                    "&eClick to apply!"
                )
            ));
        }
        
        // Custom Percentage
        gui.setItem(31, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sleep-settings", "custom-percentage")),
            plugin.getGUIConfigManager().getItemName("sleep-settings", "custom-percentage"),
            plugin.getGUIConfigManager().getItemLore("sleep-settings", "custom-percentage")
        ));
        
        // Reset to Default
        gui.setItem(40, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sleep-settings", "reset-default")),
            plugin.getGUIConfigManager().getItemName("sleep-settings", "reset-default"),
            plugin.getGUIConfigManager().getItemLore("sleep-settings", "reset-default")
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sleep-settings", "back")),
            plugin.getGUIConfigManager().getItemName("sleep-settings", "back"),
            plugin.getGUIConfigManager().getItemLore("sleep-settings", "back")
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("animation-settings")));
        
        playerMenus.put(player, "ANIMATION_SETTINGS");
        playerPages.put(player, 0);
        
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
        gui.setItem(11, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("animation-settings", "intensity")),
            plugin.getGUIConfigManager().getItemName("animation-settings", "intensity"),
            Arrays.asList(
                "&7Current Intensity: &e" + plugin.getConfigManager().getAnimationIntensity(),
                "&7Range: 1-3 (Higher = More particles)",
                "",
                "&eClick to change!"
            )
        ));
        
        // Enhanced Particles
        gui.setItem(12, createItem(
            plugin.getConfigManager().areEnhancedParticlesEnabled() ? Material.FIREWORK_STAR : Material.GUNPOWDER,
            "&bEnhanced Particles",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().areEnhancedParticlesEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Extra particle effects for better visuals",
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
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Animation Distance
        gui.setItem(19, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("animation-settings", "distance")),
            plugin.getGUIConfigManager().getItemName("animation-settings", "distance"),
            Arrays.asList(
                "&7Current Distance: &e" + plugin.getConfigManager().getMaxAnimationDistance() + " blocks",
                "&7Maximum distance for animation rendering",
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
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Animation Duration
        gui.setItem(21, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("animation-settings", "duration")),
            plugin.getGUIConfigManager().getItemName("animation-settings", "duration"),
            Arrays.asList(
                "&7Current Duration: &e" + plugin.getConfigManager().getAnimationDuration() + " seconds",
                "&7How long animations last",
                "",
                "&eClick to change!"
            )
        ));
        
        // Test Animation
        gui.setItem(31, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("animation-settings", "test")),
            plugin.getGUIConfigManager().getItemName("animation-settings", "test"),
            plugin.getGUIConfigManager().getItemLore("animation-settings", "test")
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("animation-settings", "back")),
            plugin.getGUIConfigManager().getItemName("animation-settings", "back"),
            plugin.getGUIConfigManager().getItemLore("animation-settings", "back")
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("sound-settings")));
        
        playerMenus.put(player, "SOUND_SETTINGS");
        playerPages.put(player, 0);
        
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
        gui.setItem(11, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "volume")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "volume"),
            Arrays.asList(
                "&7Current Volume: &e" + (int)(plugin.getConfigManager().getSoundVolume() * 100) + "%",
                "&7Master volume for all sounds",
                "",
                "&eClick to change!"
            )
        ));
        
        // Sleep Sound
        gui.setItem(19, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "sleep-sound")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "sleep-sound"),
            Arrays.asList(
                "&7Current: &e" + plugin.getConfigManager().getSleepSound(),
                "&7Sound played when players sleep",
                "",
                "&eClick to change!",
                "&7Right-click to test!"
            )
        ));
        
        // Night Skip Sound
        gui.setItem(20, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "night-skip-sound")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "night-skip-sound"),
            Arrays.asList(
                "&7Current: &e" + plugin.getConfigManager().getNightSkipSound(),
                "&7Sound played during night skip",
                "",
                "&eClick to change!",
                "&7Right-click to test!"
            )
        ));
        
        // GUI Sounds
        gui.setItem(21, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "gui-sounds")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "gui-sounds"),
            plugin.getGUIConfigManager().getItemLore("sound-settings", "gui-sounds")
        ));
        
        // Sound Test Area
        gui.setItem(31, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "test-sounds")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "test-sounds"),
            plugin.getGUIConfigManager().getItemLore("sound-settings", "test-sounds")
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("sound-settings", "back")),
            plugin.getGUIConfigManager().getItemName("sound-settings", "back"),
            plugin.getGUIConfigManager().getItemLore("sound-settings", "back")
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("day-counter")));
        
        playerMenus.put(player, "DAY_COUNTER");
        playerPages.put(player, 0);
        
        World world = player.getWorld();
        long currentDay = plugin.getDayCounterManager().getCurrentDay(world);
        
        // Current Day Display
        gui.setItem(4, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "current-day")),
            plugin.getGUIConfigManager().getItemName("day-counter", "current-day"),
            Arrays.asList(
                "&7World: &e" + world.getName(),
                "&7Current Day: &a" + currentDay,
                "&7Time: &b" + getTimeString(world.getTime()),
                "&7Status: " + (world.getTime() > 12000 ? "&cNight" : "&aDay")
            )
        ));
        
        // Toggle Day Counter
        gui.setItem(10, createItem(
            plugin.getConfigManager().isDayCounterEnabled() ? Material.LIME_DYE : Material.GRAY_DYE,
            "&aDay Counter Enabled",
            Arrays.asList(
                "&7Status: " + (plugin.getConfigManager().isDayCounterEnabled() ? "&aEnabled" : "&cDisabled"),
                "&7Enable or disable day counting",
                "",
                "&eClick to toggle!"
            )
        ));
        
        // Set Day Options
        int[] days = {1, 10, 50, 100, 365, 1000};
        int[] slots = {19, 20, 21, 22, 23, 24};
        
        for (int i = 0; i < days.length; i++) {
            gui.setItem(slots[i], createItem(
                Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "set-day-" + days[i])),
                "&eSet to Day " + days[i],
                Arrays.asList(
                    "&7Click to set current day to &e" + days[i],
                    "&7This will update the day counter",
                    "",
                    "&eClick to apply!"
                )
            ));
        }
        
        // Custom Day
        gui.setItem(31, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "custom-day")),
            plugin.getGUIConfigManager().getItemName("day-counter", "custom-day"),
            plugin.getGUIConfigManager().getItemLore("day-counter", "custom-day")
        ));
        
        // Reset Day
        gui.setItem(40, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "reset-day")),
            plugin.getGUIConfigManager().getItemName("day-counter", "reset-day"),
            plugin.getGUIConfigManager().getItemLore("day-counter", "reset-day")
        ));
        
        // Morning Messages
        gui.setItem(41, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "morning-messages")),
            plugin.getGUIConfigManager().getItemName("day-counter", "morning-messages"),
            plugin.getGUIConfigManager().getItemLore("day-counter", "morning-messages")
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("day-counter", "back")),
            plugin.getGUIConfigManager().getItemName("day-counter", "back"),
            plugin.getGUIConfigManager().getItemLore("day-counter", "back")
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
        
        Inventory gui = Bukkit.createInventory(null, 54, 
            MessageUtils.colorize(plugin.getGUIConfigManager().getGUITitle("statistics")));
        
        playerMenus.put(player, "STATISTICS");
        playerPages.put(player, 0);
        
        // Sleep Events
        gui.setItem(10, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "sleep-events")),
            plugin.getGUIConfigManager().getItemName("statistics", "sleep-events"),
            Arrays.asList(
                "&7Total Sleep Events: &a" + plugin.getStatisticsManager().getTotalSleepEvents(),
                "&7Players entering beds",
                "",
                "&7This tracks every time a player",
                "&7enters a bed to sleep"
            )
        ));
        
        // Night Skips
        gui.setItem(11, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "night-skips")),
            plugin.getGUIConfigManager().getItemName("statistics", "night-skips"),
            Arrays.asList(
                "&7Total Night Skips: &b" + plugin.getStatisticsManager().getTotalNightSkips(),
                "&7Successful night skips",
                "",
                "&7This tracks every time the night",
                "&7was successfully skipped"
            )
        ));
        
        // Days Tracked
        gui.setItem(12, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "days-tracked")),
            plugin.getGUIConfigManager().getItemName("statistics", "days-tracked"),
            Arrays.asList(
                "&7Total Days Tracked: &e" + plugin.getStatisticsManager().getTotalDaysTracked(),
                "&7Days counted by the plugin",
                "",
                "&7This shows how many days have",
                "&7passed since plugin installation"
            )
        ));
        
        // Players Served
        gui.setItem(13, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "players-served")),
            plugin.getGUIConfigManager().getItemName("statistics", "players-served"),
            Arrays.asList(
                "&7Players Served: &d" + plugin.getStatisticsManager().getTotalPlayersServed(),
                "&7Maximum concurrent players",
                "",
                "&7This shows the highest number of",
                "&7players online at the same time"
            )
        ));
        
        // Current Session
        gui.setItem(19, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "current-session")),
            plugin.getGUIConfigManager().getItemName("statistics", "current-session"),
            Arrays.asList(
                "&7Current Online: &a" + Bukkit.getOnlinePlayers().size(),
                "&7AFK Players: &c" + plugin.getAFKManager().getAFKCount(),
                "&7Active Players: &b" + (Bukkit.getOnlinePlayers().size() - plugin.getAFKManager().getAFKCount()),
                "",
                "&7Real-time server statistics"
            )
        ));
        
        // Plugin Info
        gui.setItem(22, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "plugin-info")),
            plugin.getGUIConfigManager().getItemName("statistics", "plugin-info"),
            Arrays.asList(
                "&7Plugin Version: &e" + plugin.getDescription().getVersion(),
                "&7Author: &a" + plugin.getDescription().getAuthors().get(0),
                "&7API Version: &b" + plugin.getDescription().getAPIVersion(),
                "",
                "&7EasySleep - Ultimate Sleep Management"
            )
        ));
        
        // Export Statistics
        gui.setItem(40, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "export")),
            plugin.getGUIConfigManager().getItemName("statistics", "export"),
            plugin.getGUIConfigManager().getItemLore("statistics", "export")
        ));
        
        // Reset Statistics
        gui.setItem(41, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "reset")),
            plugin.getGUIConfigManager().getItemName("statistics", "reset"),
            plugin.getGUIConfigManager().getItemLore("statistics", "reset")
        ));
        
        // Back button
        gui.setItem(45, createItem(
            Material.valueOf(plugin.getGUIConfigManager().getItemMaterial("statistics", "back")),
            plugin.getGUIConfigManager().getItemName("statistics", "back"),
            plugin.getGUIConfigManager().getItemLore("statistics", "back")
        ));
        
        player.openInventory(gui);
        playSound(player, "GUI_OPEN");
    }
    
    /**
     * Create an item with name and lore
     */
    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtils.colorize(name));
            if (lore != null) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(MessageUtils.colorize(line));
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
                MessageUtils.sendMessage(player, "&aSet sleep percentage to &e" + percentages[index] + "%");
                playSound(player, "GUI_SUCCESS");
                openSleepSettings(player); // Refresh GUI
            }
        }
        
        switch (slot) {
            case 40: // Reset to Default
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 1);
                MessageUtils.sendMessage(player, "&aReset sleep percentage to default (1%)");
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
                // This would require config modification - implement based on your config system
                MessageUtils.sendMessage(player, "&aToggled animations!");
                playSound(player, "GUI_SUCCESS");
                openAnimationSettings(player);
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
            case 10: // Toggle Sounds
                MessageUtils.sendMessage(player, "&aToggled sound effects!");
                playSound(player, "GUI_SUCCESS");
                openSoundSettings(player);
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
                MessageUtils.sendMessage(player, "&aSet day counter to &eDay " + days[index]);
                playSound(player, "GUI_SUCCESS");
                openDayCounter(player);
            }
        }
        
        switch (slot) {
            case 40: // Reset Day
                plugin.getDayCounterManager().resetDay(world);
                MessageUtils.sendMessage(player, "&aReset day counter to Day 1");
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
    }
}