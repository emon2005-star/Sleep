package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages day counting and displays for each world
 * 
 * @author Turjo
 * @version 1.3.1
 */
public class DayCounterManager {
    
    private final EasySleep plugin;
    private final Map<String, Long> worldDays;
    private final Map<String, Long> lastTimeCheck;
    private File dataFile;
    private FileConfiguration dataConfig;
    
    public DayCounterManager(EasySleep plugin) {
        this.plugin = plugin;
        this.worldDays = new HashMap<>();
        this.lastTimeCheck = new HashMap<>();
        
        setupDataFile();
        loadDayData();
        startDayTracker();
    }
    
    /**
     * Setup the data file for storing day counts
     */
    private void setupDataFile() {
        dataFile = new File(plugin.getDataFolder(), "daydata.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create day data file: " + e.getMessage());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
    
    /**
     * Load day data from file
     */
    private void loadDayData() {
        for (World world : Bukkit.getWorlds()) {
            String worldName = world.getName();
            long days = dataConfig.getLong("worlds." + worldName + ".days", 1);
            long lastTime = dataConfig.getLong("worlds." + worldName + ".lastTime", world.getTime());
            
            worldDays.put(worldName, days);
            lastTimeCheck.put(worldName, lastTime);
        }
    }
    
    /**
     * Save day data to file
     */
    private void saveDayData() {
        for (Map.Entry<String, Long> entry : worldDays.entrySet()) {
            String worldName = entry.getKey();
            long days = entry.getValue();
            long lastTime = lastTimeCheck.getOrDefault(worldName, 0L);
            
            dataConfig.set("worlds." + worldName + ".days", days);
            dataConfig.set("worlds." + worldName + ".lastTime", lastTime);
        }
        
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save day data: " + e.getMessage());
        }
    }
    
    /**
     * Start the day tracking system
     */
    private void startDayTracker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().isDayCounterEnabled()) {
                    return;
                }
                
                for (World world : Bukkit.getWorlds()) {
                    checkDayChange(world);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Check every second
    }
    
    /**
     * Check if a new day has started in the world
     */
    private void checkDayChange(World world) {
        String worldName = world.getName();
        long currentTime = world.getTime();
        long lastTime = lastTimeCheck.getOrDefault(worldName, currentTime);
        
        // Check if time has wrapped around (new day)
        if (lastTime > 12000 && currentTime < 6000) {
            // New day detected
            long currentDay = worldDays.getOrDefault(worldName, 1L);
            long newDay = currentDay + 1;
            
            worldDays.put(worldName, newDay);
            lastTimeCheck.put(worldName, currentTime);
            
            // Show day title to all players in the world
            showDayTitle(world, newDay);
            
            // Save data
            saveDayData();
            
            if (plugin.getConfigManager().isDebugMode()) {
                plugin.getLogger().info("New day detected in " + worldName + ": Day " + newDay);
            }
        } else {
            lastTimeCheck.put(worldName, currentTime);
        }
    }
    
    /**
     * Show day title to all players in the world
     */
    private void showDayTitle(World world, long day) {
        // Get random morning message
        java.util.List<String> messages = plugin.getConfigManager().getRandomMorningMessages();
        String message = "Day " + day + " - A new adventure begins!";
        
        if (!messages.isEmpty()) {
            java.util.Random random = new java.util.Random();
            message = messages.get(random.nextInt(messages.size()));
            message = message.replace("%day%", String.valueOf(day));
        }
        
        String title = "§fDay " + day;
        String subtitle = "§7" + (message.contains(" - ") ? message.substring(message.indexOf(" - ") + 3) : "A new adventure begins!");
        
        for (Player player : world.getPlayers()) {
            player.sendTitle(title, subtitle, 10, 60, 20);
        }
        
        if (plugin.getConfigManager().isDebugMode()) {
            plugin.getLogger().info("Displayed day title for " + world.getName() + ": " + title);
        }
    }
    
    /**
     * Get the current day for a world
     */
    public long getCurrentDay(World world) {
        return worldDays.getOrDefault(world.getName(), 1L);
    }
    
    /**
     * Set the day for a world (admin command)
     */
    public void setDay(World world, long day) {
        worldDays.put(world.getName(), day);
        saveDayData();
    }
    
    /**
     * Reset day counter for a world
     */
    public void resetDay(World world) {
        worldDays.put(world.getName(), 1L);
        lastTimeCheck.put(world.getName(), world.getTime());
        saveDayData();
    }
    
    /**
     * Cleanup method for plugin disable
     */
    public void cleanup() {
        saveDayData();
    }
}