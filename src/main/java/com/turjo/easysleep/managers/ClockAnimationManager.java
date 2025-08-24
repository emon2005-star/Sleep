package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Beautiful Clock Animation System
 * Displays floating particle clocks showing real game time
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class ClockAnimationManager {
    
    private final EasySleep plugin;
    private BukkitRunnable clockTask;
    
    public ClockAnimationManager(EasySleep plugin) {
        this.plugin = plugin;
        startClockAnimation();
    }
    
    /**
     * Start the clock animation system
     */
    private void startClockAnimation() {
        if (!plugin.getConfigManager().isClockAnimationEnabled()) {
            return;
        }
        
        clockTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!plugin.getConfigManager().isClockAnimationEnabled()) {
                    return;
                }
                
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.isSleeping()) {
                        showClockAnimation(player);
                    }
                }
                
                // Play chime sound every 30 seconds
                if (ticks % 600 == 0) {
                    playClockChime();
                }
                
                ticks++;
            }
        };
        clockTask.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Show clock animation for a player
     */
    private void showClockAnimation(Player player) {
        // Skip if performance mode is enabled
        if (plugin.getConfigManager().isPerformanceMode()) {
            return;
        }
        
        World world = player.getWorld();
        Location loc = player.getLocation().add(0, 3, 0);
        long time = world.getTime();
        
        // Convert Minecraft time to 12-hour format
        double hours = ((time + 6000) % 24000) / 1000.0;
        double minutes = (hours % 1) * 60;
        
        // Create clock face
        createClockFace(loc, world);
        
        // Create hour hand
        createClockHand(loc, world, hours * 30, 0.8, getTimeParticle(time)); // 30 degrees per hour
        
        // Create minute hand
        createClockHand(loc, world, minutes * 6, 1.2, getTimeParticle(time)); // 6 degrees per minute
        
        // Center dot
        world.spawnParticle(Particle.END_ROD, loc, 1, 0, 0, 0, 0);
    }
    
    /**
     * Create clock face with hour markers
     */
    private void createClockFace(Location center, World world) {
        // Reduce markers for performance
        for (int i = 0; i < 4; i++) { // Only show 4 main markers instead of 12
            double angle = i * 30 * Math.PI / 180; // 30 degrees per hour
            double x = Math.cos(angle) * 1.0; // Smaller radius
            double z = Math.sin(angle) * 1.0;
            Location markerLoc = center.clone().add(x, 0, z);
            
            world.spawnParticle(Particle.VILLAGER_HAPPY, markerLoc, 1, 0, 0, 0, 0);
        }
    }
    
    /**
     * Create clock hand
     */
    private void createClockHand(Location center, World world, double angleDegrees, double length, Particle particle) {
        double angle = (angleDegrees - 90) * Math.PI / 180; // -90 to start from top
        
        // Reduce hand detail for performance
        for (double i = 0.3; i <= length; i += 0.3) { // Larger steps, fewer particles
            double x = Math.cos(angle) * i;
            double z = Math.sin(angle) * i;
            Location handLoc = center.clone().add(x, 0, z);
            
            world.spawnParticle(particle, handLoc, 1, 0, 0, 0, 0);
        }
    }
    
    /**
     * Get appropriate particle based on time of day
     */
    private Particle getTimeParticle(long time) {
        if (time >= 0 && time < 12000) {
            return Particle.FLAME; // Day - warm particles
        } else {
            return Particle.SOUL_FIRE_FLAME; // Night - cool particles
        }
    }
    
    /**
     * Play gentle clock chime
     */
    private void playClockChime() {
        if (!plugin.getConfigManager().areSoundEffectsEnabled()) {
            return;
        }
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.isSleeping()) {
                float volume = (float) (0.1f * plugin.getConfigManager().getSoundVolume());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, volume, 2.0f);
            }
        }
    }
    
    /**
     * Restart clock animation (for config reload)
     */
    public void restart() {
        if (clockTask != null && !clockTask.isCancelled()) {
            clockTask.cancel();
        }
        startClockAnimation();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        if (clockTask != null && !clockTask.isCancelled()) {
            clockTask.cancel();
        }
    }
}