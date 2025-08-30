package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Advanced AFK Detection System
 * Tracks player movement and activity to determine AFK status
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class AFKManager {
    
    private final EasySleep plugin;
    private final Map<UUID, PlayerActivity> playerActivity;
    private final Map<UUID, Boolean> afkPlayers;
    
    public AFKManager(EasySleep plugin) {
        this.plugin = plugin;
        this.playerActivity = new HashMap<>();
        this.afkPlayers = new HashMap<>();
        startAFKChecker();
    }
    
    /**
     * Update player activity
     */
    public void updateActivity(Player player) {
        if (!plugin.getConfigManager().isAFKDetectionEnabled()) {
            return;
        }
        
        UUID uuid = player.getUniqueId();
        Location currentLoc = player.getLocation();
        long currentTime = System.currentTimeMillis();
        
        PlayerActivity activity = playerActivity.get(uuid);
        if (activity == null) {
            activity = new PlayerActivity(currentLoc, currentTime);
            playerActivity.put(uuid, activity);
        } else {
            // Check if player moved significantly (with world safety check)
            if (isSameWorld(activity.lastLocation, currentLoc) && 
                activity.lastLocation.distance(currentLoc) > 1.0) {
                activity.lastLocation = currentLoc;
                activity.lastActivity = currentTime;
                
                // Remove from AFK if they were AFK
                if (afkPlayers.getOrDefault(uuid, false)) {
                    afkPlayers.put(uuid, false);
                    if (plugin.getConfigManager().isDebugMode()) {
                        plugin.getLogger().info(player.getName() + " is no longer AFK");
                    }
                }
            } else if (!isSameWorld(activity.lastLocation, currentLoc)) {
                // Player changed worlds - update location without distance check
                activity.lastLocation = currentLoc;
                activity.lastActivity = currentTime;
            }
        }
    }
    
    /**
     * Check if two locations are in the same world (Multiverse compatibility)
     */
    private boolean isSameWorld(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return false;
        }
        if (loc1.getWorld() == null || loc2.getWorld() == null) {
            return false;
        }
        return loc1.getWorld().equals(loc2.getWorld());
    }
    
    /**
     * Check if player is AFK
     */
    public boolean isPlayerAFK(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * Get AFK threshold in milliseconds
     */
    private long getAFKThreshold() {
        return plugin.getConfigManager().getAFKThreshold() * 60 * 1000L; // Convert minutes to milliseconds
    }
    
    /**
     * Start AFK checking task
     */
    private void startAFKChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().isAFKDetectionEnabled()) {
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                long threshold = getAFKThreshold();
                
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    PlayerActivity activity = playerActivity.get(uuid);
                    
                    if (activity != null) {
                        boolean wasAFK = afkPlayers.getOrDefault(uuid, false);
                        boolean isNowAFK = (currentTime - activity.lastActivity) > threshold;
                        
                        if (!wasAFK && isNowAFK) {
                            afkPlayers.put(uuid, true);
                            if (plugin.getConfigManager().isDebugMode()) {
                                plugin.getLogger().info(player.getName() + " is now AFK");
                            }
                        }
                    } else {
                        // Initialize activity for new players
                        updateActivity(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 200L); // Check every 10 seconds
    }
    
    /**
     * Remove player from tracking
     */
    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        playerActivity.remove(uuid);
        afkPlayers.remove(uuid);
    }
    
    /**
     * Get total AFK players count
     */
    public int getAFKCount() {
        return (int) afkPlayers.values().stream().mapToInt(afk -> afk ? 1 : 0).sum();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        playerActivity.clear();
        afkPlayers.clear();
    }
    
    /**
     * Player activity data class
     */
    private static class PlayerActivity {
        Location lastLocation;
        long lastActivity;
        
        PlayerActivity(Location location, long time) {
            this.lastLocation = location;
            this.lastActivity = time;
        }
    }
}