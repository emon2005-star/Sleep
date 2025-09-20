package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Modern Minimalist Clock Animation System
 * Ultra-lightweight clock display with modern aesthetics
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
     * Start modern clock animation system
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
                
                // Only show clock every 60 ticks (3 seconds) to reduce lag
                if (ticks % 60 == 0) {
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        if (player.isSleeping()) {
                            showModernClock(player);
                        }
                    }
                }
                
                // Gentle chime every 5 minutes (6000 ticks)
                if (ticks % 6000 == 0) {
                    playModernClockChime();
                }
                
                ticks++;
            }
        };
        clockTask.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Show modern minimalist clock
     */
    private void showModernClock(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation().add(0, 2.5, 0);
        long time = world.getTime();
        
        // Convert to 12-hour format
        double hours = ((time + 6000) % 24000) / 1000.0;
        double minutes = (hours % 1) * 60;
        
        // Modern clock design - only 4 cardinal points
        createModernClockFace(loc, world);
        
        // Single particle for hour hand
        createModernClockHand(loc, world, hours * 30, 0.6, getModernTimeParticle(time));
        
        // Single particle for minute hand  
        createModernClockHand(loc, world, minutes * 6, 0.9, getModernTimeParticle(time));
        
        // Center point - single particle
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0, 0, 0, 0);
    }
    
    /**
     * Create modern clock face - only 4 markers
     */
    private void createModernClockFace(Location center, World world) {
        // Only show 4 main positions (12, 3, 6, 9 o'clock)
        for (int i = 0; i < 4; i++) {
            double angle = i * 90 * Math.PI / 180;
            double x = Math.cos(angle) * 0.8;
            double z = Math.sin(angle) * 0.8;
            Location markerLoc = center.clone().add(x, 0, z);
            
            world.spawnParticle(Particle.END_ROD, markerLoc, 1, 0, 0, 0, 0);
        }
    }
    
    /**
     * Create modern clock hand - single particle
     */
    private void createModernClockHand(Location center, World world, double angleDegrees, double length, Particle particle) {
        double angle = (angleDegrees - 90) * Math.PI / 180;
        
        // Only one particle at the end of the hand
        double x = Math.cos(angle) * length;
        double z = Math.sin(angle) * length;
        Location handLoc = center.clone().add(x, 0, z);
        
        world.spawnParticle(particle, handLoc, 1, 0, 0, 0, 0);
    }
    
    /**
     * Get modern time-appropriate particle
     */
    private Particle getModernTimeParticle(long time) {
        if (time >= 0 && time < 12000) {
            return Particle.SOUL_FIRE_FLAME; // Day - cool blue flames
        } else {
            return Particle.SOUL; // Night - mystical souls
        }
    }
    
    /**
     * Play modern gentle clock chime
     */
    private void playModernClockChime() {
        if (!plugin.getConfigManager().areSoundEffectsEnabled()) {
            return;
        }
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.isSleeping()) {
                float volume = (float) (0.05f * plugin.getConfigManager().getSoundVolume());
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, volume, 2.2f);
            }
        }
    }
    
    /**
     * Restart clock animation
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
        if (clockTask != null) {
            clockTask.cancel();
            clockTask.cancel();
        }
    }
}