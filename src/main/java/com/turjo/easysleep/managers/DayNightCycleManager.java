package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Modern Day-Night Cycle Manager
 * Ultra-lightweight time acceleration effects
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class DayNightCycleManager {
    
    private final EasySleep plugin;
    private final Map<String, Long> lastTimeCheck;
    private final Map<String, Boolean> animationActive;
    
    public DayNightCycleManager(EasySleep plugin) {
        this.plugin = plugin;
        this.lastTimeCheck = new HashMap<>();
        this.animationActive = new HashMap<>();
        startModernCycleMonitoring();
    }
    
    /**
     * Start modern cycle monitoring
     */
    private void startModernCycleMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().isDayNightAnimationEnabled()) {
                    return;
                }
                
                for (World world : plugin.getServer().getWorlds()) {
                    checkModernTimeAcceleration(world);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second instead of every 0.5 seconds
    }
    
    /**
     * Check for time acceleration
     */
    private void checkModernTimeAcceleration(World world) {
        String worldName = world.getName();
        long currentTime = world.getTime();
        long lastTime = lastTimeCheck.getOrDefault(worldName, currentTime);
        
        // Detect time jump
        long timeDiff = Math.abs(currentTime - lastTime);
        
        if (timeDiff > 200 && !animationActive.getOrDefault(worldName, false)) {
            triggerModernTimeAcceleration(world);
            animationActive.put(worldName, true);
            
            // Reset flag after 5 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    animationActive.put(worldName, false);
                }
            }.runTaskLater(plugin, 100L);
        }
        
        lastTimeCheck.put(worldName, currentTime);
    }
    
    /**
     * Trigger modern time acceleration
     */
    private void triggerModernTimeAcceleration(World world) {
        if (plugin.getConfigManager().isDebugMode()) {
            plugin.getLogger().info("Modern time acceleration in " + world.getName());
        }
        
        // Modern announcement
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "&b⚡ &f&lTIME ACCELERATION &b&lACTIVE &b⚡");
        MessageUtils.broadcastToWorld(world, "&7🌀 &eReality shifting... &7🌀");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "");
        
        startModernAccelerationSequence(world);
    }
    
    /**
     * Modern acceleration sequence - ultra minimal
     */
    private void startModernAccelerationSequence(World world) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (world.getPlayers().isEmpty()) {
                    cancel();
                    return;
                }
                
                // Only animate for one player to reduce server load
                Player player = world.getPlayers().get(0);
                Location skyLoc = player.getLocation().add(0, 6, 0);
                
                if (ticks < 40) { // First 2 seconds - acceleration
                    createModernAcceleration(player, skyLoc, ticks);
                } else if (ticks < 60) { // Next 1 second - stabilization
                    createModernStabilization(player, skyLoc, ticks - 40);
                } else {
                    // Completion
                    MessageUtils.broadcastToWorld(world, "");
                    MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    MessageUtils.broadcastToWorld(world, "&a✓ &f&lTIME ACCELERATION &a&lCOMPLETE &a✓");
                    MessageUtils.broadcastToWorld(world, "&7🌅 &eNew timeline established! &7🌅");
                    MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    MessageUtils.broadcastToWorld(world, "");
                    cancel();
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Modern acceleration effect - single rotating particle
     */
    private void createModernAcceleration(Player player, Location center, int ticks) {
        // Single particle in a slow spiral
        double angle = ticks * 0.3;
        double radius = 1.0 + Math.sin(ticks * 0.1) * 0.2;
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;
        double y = Math.sin(ticks * 0.05) * 0.3;
        
        Location accelLoc = center.clone().add(x, y, z);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, accelLoc, 1, 0, 0, 0, 0);
        
        // Sound only at start
        if (ticks == 0) {
            playModernSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.1f, 1.2f);
        }
    }
    
    /**
     * Modern stabilization effect - converging particles
     */
    private void createModernStabilization(Player player, Location center, int ticks) {
        // 3 particles converging to center
        for (int i = 0; i < 3; i++) {
            double angle = i * 120 * Math.PI / 180;
            double progress = ticks / 20.0; // 0 to 1 over 20 ticks
            double radius = 1.5 * (1.0 - progress);
            
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Location stabilizeLoc = center.clone().add(x, 0, z);
            
            player.getWorld().spawnParticle(Particle.TOTEM, stabilizeLoc, 1, 0.05, 0.05, 0.05, 0.01);
        }
        
        // Completion sound
        if (ticks == 15) {
            playModernSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.15f, 1.6f);
        }
    }
    
    /**
     * Play modern sound effects
     */
    private void playModernSound(Player player, Sound sound, float volume, float pitch) {
        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
            float adjustedVolume = (float) (volume * plugin.getConfigManager().getSoundVolume());
            player.playSound(player.getLocation(), sound, adjustedVolume, pitch);
        }
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        lastTimeCheck.clear();
        animationActive.clear();
    }
}