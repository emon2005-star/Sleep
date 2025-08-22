package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Advanced Anti-Spam Protection System
 * Prevents message and command spam during sleep events
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class AntiSpamManager {
    
    private final EasySleep plugin;
    private final Map<UUID, Long> lastSleepMessage;
    private final Map<UUID, Long> lastWakeMessage;
    private final Map<UUID, Long> lastCommand;
    
    public AntiSpamManager(EasySleep plugin) {
        this.plugin = plugin;
        this.lastSleepMessage = new HashMap<>();
        this.lastWakeMessage = new HashMap<>();
        this.lastCommand = new HashMap<>();
    }
    
    /**
     * Check if player can send sleep message
     */
    public boolean canSendSleepMessage(Player player) {
        if (!plugin.getConfigManager().isAntiSpamEnabled()) {
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastSleepMessage.getOrDefault(uuid, 0L);
        long cooldown = plugin.getConfigManager().getSleepMessageCooldown() * 1000L;
        
        if (currentTime - lastTime >= cooldown) {
            lastSleepMessage.put(uuid, currentTime);
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if player can send wake message
     */
    public boolean canSendWakeMessage(Player player) {
        if (!plugin.getConfigManager().isAntiSpamEnabled()) {
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastWakeMessage.getOrDefault(uuid, 0L);
        long cooldown = plugin.getConfigManager().getWakeMessageCooldown() * 1000L;
        
        if (currentTime - lastTime >= cooldown) {
            lastWakeMessage.put(uuid, currentTime);
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if player can use command
     */
    public boolean canUseCommand(Player player) {
        if (!plugin.getConfigManager().isAntiSpamEnabled()) {
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastCommand.getOrDefault(uuid, 0L);
        long cooldown = plugin.getConfigManager().getCommandCooldown() * 1000L;
        
        if (currentTime - lastTime >= cooldown) {
            lastCommand.put(uuid, currentTime);
            return true;
        }
        
        return false;
    }
    
    /**
     * Get remaining cooldown for sleep message
     */
    public long getSleepMessageCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastSleepMessage.getOrDefault(uuid, 0L);
        long cooldown = plugin.getConfigManager().getSleepMessageCooldown() * 1000L;
        
        return Math.max(0, cooldown - (currentTime - lastTime)) / 1000L;
    }
    
    /**
     * Get remaining cooldown for command
     */
    public long getCommandCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastCommand.getOrDefault(uuid, 0L);
        long cooldown = plugin.getConfigManager().getCommandCooldown() * 1000L;
        
        return Math.max(0, cooldown - (currentTime - lastTime)) / 1000L;
    }
    
    /**
     * Remove player from tracking
     */
    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        lastSleepMessage.remove(uuid);
        lastWakeMessage.remove(uuid);
        lastCommand.remove(uuid);
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        lastSleepMessage.clear();
        lastWakeMessage.clear();
        lastCommand.clear();
    }
}