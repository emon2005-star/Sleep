package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Advanced Statistics Tracking System
 * Tracks plugin usage and sleep statistics
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class StatisticsManager {
    
    private final EasySleep plugin;
    private File statsFile;
    private FileConfiguration statsConfig;
    
    // Statistics counters
    private long totalSleepEvents = 0;
    private long totalNightSkips = 0;
    private long totalDaysTracked = 0;
    private long totalPlayersServed = 0;
    
    public StatisticsManager(EasySleep plugin) {
        this.plugin = plugin;
        setupStatsFile();
        loadStatistics();
    }
    
    /**
     * Setup statistics file
     */
    private void setupStatsFile() {
        statsFile = new File(plugin.getDataFolder(), "statistics.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create statistics file: " + e.getMessage());
            }
        }
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }
    
    /**
     * Load statistics from file
     */
    private void loadStatistics() {
        totalSleepEvents = statsConfig.getLong("total-sleep-events", 0);
        totalNightSkips = statsConfig.getLong("total-night-skips", 0);
        totalDaysTracked = statsConfig.getLong("total-days-tracked", 0);
        totalPlayersServed = statsConfig.getLong("total-players-served", 0);
    }
    
    /**
     * Save statistics to file
     */
    private void saveStatistics() {
        statsConfig.set("total-sleep-events", totalSleepEvents);
        statsConfig.set("total-night-skips", totalNightSkips);
        statsConfig.set("total-days-tracked", totalDaysTracked);
        statsConfig.set("total-players-served", totalPlayersServed);
        statsConfig.set("last-updated", System.currentTimeMillis());
        
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save statistics: " + e.getMessage());
        }
    }
    
    /**
     * Record a sleep event
     */
    public void recordSleepEvent() {
        totalSleepEvents++;
        saveStatistics();
    }
    
    /**
     * Record a night skip
     */
    public void recordNightSkip() {
        totalNightSkips++;
        saveStatistics();
    }
    
    /**
     * Record a new day
     */
    public void recordNewDay() {
        totalDaysTracked++;
        saveStatistics();
    }
    
    /**
     * Update player count
     */
    public void updatePlayerCount(int currentPlayers) {
        if (currentPlayers > totalPlayersServed) {
            totalPlayersServed = currentPlayers;
            saveStatistics();
        }
    }
    
    // Getters
    public long getTotalSleepEvents() { return totalSleepEvents; }
    public long getTotalNightSkips() { return totalNightSkips; }
    public long getTotalDaysTracked() { return totalDaysTracked; }
    public long getTotalPlayersServed() { return totalPlayersServed; }
    
    /**
     * Get formatted statistics string
     */
    public String getFormattedStats() {
        return String.format(
            "Sleep Events: %d | Night Skips: %d | Days Tracked: %d | Players Served: %d",
            totalSleepEvents, totalNightSkips, totalDaysTracked, totalPlayersServed
        );
    }
}