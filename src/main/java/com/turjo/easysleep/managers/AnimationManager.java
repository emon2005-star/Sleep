package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Enhanced Animation Manager with Gentle Effects
 * Manages beautiful, non-intrusive animations for sleep events
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class AnimationManager {
    
    private final EasySleep plugin;
    private final Map<Player, BukkitTask> activeAnimations;
    private final Random random;
    
    public AnimationManager(EasySleep plugin) {
        this.plugin = plugin;
        this.activeAnimations = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * Start gentle sleep animation for a player
     */
    public void startSleepAnimation(Player player) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        stopAnimation(player); // Stop any existing animation
        
        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.isSleeping()) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 1.5, 0);
                
                // Gentle floating particles - much less intensive
                if (ticks % 20 == 0) { // Every second instead of every 10 ticks
                    createGentleSleepEffects(player, loc);
                }
                
                // Soft sound effects - less frequent
                if (ticks % 80 == 0) { // Every 4 seconds instead of 2
                    playGentleSleepSound(player);
                }
                
                ticks++;
                
                // Stop after 10 seconds instead of 30
                if (ticks > 200) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        activeAnimations.put(player, task);
    }
    
    /**
     * Create gentle sleep effects - much more subtle
     */
    private void createGentleSleepEffects(Player player, Location center) {
        if (!isPlayerInRange(player, center, 16)) { // Reduced range for performance
            return;
        }
        
        int intensity = plugin.getConfigManager().getAnimationIntensity();
        double density = plugin.getConfigManager().getParticleDensity();
        
        // Drastically reduce particle count for performance
        int particleCount = Math.max(1, (int)(intensity * density * 0.1)); // Even lower multiplier
        
        // Performance mode reduces particles even more
        if (plugin.getConfigManager().isPerformanceMode()) {
            particleCount = Math.max(1, particleCount / 2);
        }
        
        // Gentle floating hearts - extremely sparse
        for (int i = 0; i < Math.min(particleCount, 1); i++) { // Max 1 particle at a time
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = 0.5 + random.nextDouble() * 0.3; // Even smaller radius
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = random.nextDouble() * 0.2; // Even lower height variation
            
            Location particleLoc = center.clone().add(x, y, z);
            
            try {
                Particle sleepParticle = Particle.valueOf(plugin.getConfigManager().getSleepParticle());
                player.getWorld().spawnParticle(sleepParticle, particleLoc, 1, 0.05, 0.05, 0.05, 0.005);
            } catch (IllegalArgumentException e) {
                player.getWorld().spawnParticle(Particle.HEART, particleLoc, 1, 0.05, 0.05, 0.05, 0.005);
            }
        }
        
        // Optional enhanced particles - extremely minimal
        if (plugin.getConfigManager().areEnhancedParticlesEnabled() && 
            !plugin.getConfigManager().isPerformanceMode() && 
            random.nextDouble() < 0.1) { // Much lower chance
            player.getWorld().spawnParticle(Particle.END_ROD, center, 1, 0.1, 0.1, 0.1, 0.005);
        }
    }
    
    /**
     * Play gentle sleep sound
     */
    private void playGentleSleepSound(Player player) {
        if (!plugin.getConfigManager().areSoundEffectsEnabled()) {
            return;
        }
        
        float volume = (float) (0.15f * plugin.getConfigManager().getSoundVolume()); // Even quieter
        try {
            Sound sleepSound = Sound.valueOf(plugin.getConfigManager().getSleepSound());
            player.playSound(player.getLocation(), sleepSound, volume, 1.8f);
        } catch (IllegalArgumentException e) {
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, volume, 1.8f);
        }
    }
    
    /**
     * Start enhanced night skip animation with time acceleration effect
     */
    public void startNightSkipAnimation(World world) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        // Enhanced futuristic messages
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "&b⚡ &f&lTIME ACCELERATION PROTOCOL &b&lACTIVATED &b⚡");
        MessageUtils.broadcastToWorld(world, "&7🌀 &eReality bending... Time streams converging... &7🌀");
        MessageUtils.broadcastToWorld(world, "&7⏰ &fTemporal status: &a&lACCELERATING &7⏰");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "");
        
        // Start the time acceleration animation
        startTimeAccelerationAnimation(world);
        
        // Start gentle effects for sleeping players
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                startGentleNightSkipAnimation(player);
            } else {
                startAwakePlayerEffects(player);
            }
        }
    }
    
    /**
     * Time acceleration animation - like the image shows
     */
    private void startTimeAccelerationAnimation(World world) {
        // Skip animation if performance mode is enabled
        if (plugin.getConfigManager().isPerformanceMode()) {
            MessageUtils.broadcastToWorld(world, "&a✓ Night skipped! (Performance mode - animations disabled)");
            return;
        }
        
        new BukkitRunnable() {
            int phase = 0;
            int ticks = 0;
            
            @Override
            public void run() {
                if (world.getPlayers().isEmpty()) {
                    cancel();
                    return;
                }
                
                for (Player player : world.getPlayers()) {
                    Location loc = player.getLocation().add(0, 10, 0); // High in the sky
                    
                    switch (phase) {
                        case 0: // Time vortex formation
                            createTimeVortex(player, loc, ticks);
                            break;
                        case 1: // Day-night cycle acceleration
                            createDayNightCycle(player, loc, ticks);
                            break;
                        case 2: // Time stabilization
                            createTimeStabilization(player, loc, ticks);
                            break;
                    }
                }
                
                ticks++;
                
                // Move to next phase every 60 ticks (3 seconds)
                if (ticks >= 60) {
                    phase++;
                    ticks = 0;
                    
                    if (phase >= 3) {
                        // Final completion message
                        MessageUtils.broadcastToWorld(world, "");
                        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        MessageUtils.broadcastToWorld(world, "&a✓ &f&lTIME ACCELERATION &a&lCOMPLETE &a✓");
                        MessageUtils.broadcastToWorld(world, "&7⚡ &eWelcome to the new dawn! Time flows normally again. &7⚡");
                        MessageUtils.broadcastToWorld(world, "&7🌅 &fTemporal status: &a&lSTABILIZED &7🌅");
                        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        MessageUtils.broadcastToWorld(world, "");
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Create time vortex effect in the sky
     */
    private void createTimeVortex(Player player, Location center, int ticks) {
        if (!isPlayerInRange(player, center, 32)) return;
        
        World world = player.getWorld();
        double radius = 2.0 + Math.sin(ticks * 0.1) * 0.3; // Smaller radius
        
        // Reduced particle count for performance
        int particleCount = plugin.getConfigManager().isPerformanceMode() ? 6 : 12;
        
        // Circular time vortex - much fewer particles
        for (int i = 0; i < particleCount; i++) {
            double angle = (ticks * 0.2 + i * 22.5) * Math.PI / 180;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = Math.sin(ticks * 0.05 + i) * 0.5; // Reduced height
            
            Location vortexLoc = center.clone().add(x, y, z);
            
            world.spawnParticle(Particle.PORTAL, vortexLoc, 1, 0.05, 0.05, 0.05, 0.01);
            
            // Reduced enhanced effects
            if (i % 6 == 0 && !plugin.getConfigManager().isPerformanceMode()) {
                world.spawnParticle(Particle.END_ROD, vortexLoc, 1, 0.02, 0.02, 0.02, 0.005);
            }
        }
        
        // Play atmospheric sound
        if (ticks == 0) {
            playTimeSound(player, Sound.BLOCK_PORTAL_AMBIENT, 0.3f, 0.8f);
        }
    }
    
    /**
     * Create day-night cycle acceleration effect
     */
    private void createDayNightCycle(Player player, Location center, int ticks) {
        if (!isPlayerInRange(player, center, 32)) return;
        
        World world = player.getWorld();
        
        // Simplified sun/moon cycle representation
        double cycleProgress = (ticks % 40) / 40.0; // Complete cycle every 2 seconds
        double sunAngle = cycleProgress * 2 * Math.PI;
        
        // Reduced size sun position
        double sunX = Math.cos(sunAngle) * 2.5;
        double sunY = Math.sin(sunAngle) * 1.5;
        Location sunLoc = center.clone().add(sunX, sunY, 0);
        
        // Reduced size moon position (opposite side)
        double moonX = Math.cos(sunAngle + Math.PI) * 2.5;
        double moonY = Math.sin(sunAngle + Math.PI) * 1.5;
        Location moonLoc = center.clone().add(moonX, moonY, 0);
        
        // Reduced sun particles (day)
        if (sunY > 0) {
            world.spawnParticle(Particle.FLAME, sunLoc, 1, 0.1, 0.1, 0.1, 0.005);
            if (!plugin.getConfigManager().isPerformanceMode()) {
                world.spawnParticle(Particle.LAVA, sunLoc, 1, 0.05, 0.05, 0.05, 0);
            }
        }
        
        // Reduced moon particles (night)
        if (moonY > 0) {
            world.spawnParticle(Particle.END_ROD, moonLoc, 1, 0.05, 0.05, 0.05, 0.005);
            if (!plugin.getConfigManager().isPerformanceMode()) {
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, moonLoc, 1, 0.05, 0.05, 0.05, 0);
            }
        }
        
        // Reduced acceleration trail
        if (!plugin.getConfigManager().isPerformanceMode()) {
            for (int i = 1; i <= 2; i++) {
            double trailAngle = sunAngle - (i * 0.3);
            double trailX = Math.cos(trailAngle) * 2.5;
            double trailY = Math.sin(trailAngle) * 1.5;
            if (trailY > 0) {
                Location trailLoc = center.clone().add(trailX, trailY, 0);
                world.spawnParticle(Particle.FIREWORKS_SPARK, trailLoc, 1, 0.05, 0.05, 0.05, 0.01);
                }
            }
        }
        
        // Play cycle sound
        if (ticks % 20 == 0) {
            playTimeSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 0.2f, 1.5f);
        }
    }
    
    /**
     * Create time stabilization effect
     */
    private void createTimeStabilization(Player player, Location center, int ticks) {
        if (!isPlayerInRange(player, center, 32)) return;
        
        World world = player.getWorld();
        
        // Reduced converging energy streams
        int streamCount = plugin.getConfigManager().isPerformanceMode() ? 6 : 8;
        
        for (int i = 0; i < streamCount; i++) {
            double progress = Math.min(1.0, ticks / 60.0);
            double angle = i * 30 * Math.PI / 180;
            double startRadius = 3.0; // Smaller radius
            double currentRadius = startRadius * (1.0 - progress);
            
            double x = Math.cos(angle) * currentRadius;
            double z = Math.sin(angle) * currentRadius;
            double y = Math.sin(progress * Math.PI) * 1.0; // Reduced height
            
            Location stabilizeLoc = center.clone().add(x, y, z);
            
            world.spawnParticle(Particle.TOTEM, stabilizeLoc, 1, 0.05, 0.05, 0.05, 0.01);
            
            if (progress > 0.8 && !plugin.getConfigManager().isPerformanceMode()) {
                world.spawnParticle(Particle.FIREWORKS_SPARK, stabilizeLoc, 1, 0.1, 0.1, 0.1, 0.02);
            }
        }
        
        // Central stabilization core
        if (ticks > 30 && !plugin.getConfigManager().isPerformanceMode()) {
            world.spawnParticle(Particle.END_ROD, center, 2, 0.15, 0.15, 0.15, 0.02);
        }
        
        // Final sound
        if (ticks == 50) {
            playTimeSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.4f, 1.2f);
        }
    }
    
    /**
     * Gentle night skip animation for sleeping players
     */
    private void startGentleNightSkipAnimation(Player player) {
        // Skip if performance mode is enabled
        if (plugin.getConfigManager().isPerformanceMode()) {
            return;
        }
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 2, 0);
                
                // Very gentle dream completion effect
                if (ticks % 20 == 0) { // Even less frequent
                    player.getWorld().spawnParticle(Particle.HEART, loc, 1, 0.1, 0.1, 0.1, 0.005);
                }
                
                if (ticks % 30 == 0) { // Much less frequent
                    player.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0.2, 0.2, 0.2, 0.01);
                }
                
                ticks++;
                if (ticks >= 40) { // Shorter duration - 2 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 1L); // Start after 1 second delay
    }
    
    /**
     * Effects for awake players during night skip
     */
    private void startAwakePlayerEffects(Player player) {
        // Skip if performance mode is enabled
        if (plugin.getConfigManager().isPerformanceMode()) {
            return;
        }
        
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                Location loc = player.getLocation().add(0, 1.5, 0);
                
                // Subtle mystical aura
                if (ticks % 25 == 0) { // Much less frequent
                    double angle = ticks * 0.2;
                    for (int i = 0; i < 2; i++) { // Fewer particles
                        double currentAngle = angle + (i * 90 * Math.PI / 180);
                        double x = Math.cos(currentAngle) * 0.5; // Smaller radius
                        double z = Math.sin(currentAngle) * 0.5;
                        Location auraLoc = loc.clone().add(x, 0, z);
                        
                        player.getWorld().spawnParticle(Particle.PORTAL, auraLoc, 1, 0.05, 0.05, 0.05, 0.005);
                    }
                }
                
                ticks++;
                if (ticks >= 60) { // Shorter duration - 3 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 40L, 1L); // Start after 2 second delay
    }
    
    /**
     * Play time-related sound effects
     */
    private void playTimeSound(Player player, Sound sound, float volume, float pitch) {
        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
            float adjustedVolume = (float) (volume * plugin.getConfigManager().getSoundVolume());
            player.playSound(player.getLocation(), sound, adjustedVolume, pitch);
        }
    }
    
    /**
     * Check if player is within animation range for performance
     */
    private boolean isPlayerInRange(Player player, Location center, int defaultRange) {
        int maxDistance = plugin.getConfigManager().getMaxAnimationDistance();
        
        // Use the smaller of the two ranges
        maxDistance = Math.min(maxDistance, defaultRange);
        
        if (plugin.getConfigManager().isPerformanceMode()) {
            maxDistance = Math.min(maxDistance, 8); // Much smaller range in performance mode
        }
        
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= maxDistance) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Stop animation for a specific player
     */
    public void stopAnimation(Player player) {
        BukkitTask task = activeAnimations.remove(player);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }
    
    /**
     * Stop all animations
     */
    public void stopAllAnimations() {
        for (BukkitTask task : activeAnimations.values()) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        activeAnimations.clear();
    }
    
    /**
     * Cleanup method for plugin disable
     */
    public void cleanup() {
        stopAllAnimations();
    }
}