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
 * Modern Performance-Optimized Animation Manager
 * Ultra-lightweight animations with modern visual effects
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
     * Modern sleep animation - minimal and elegant
     */
    public void startSleepAnimation(Player player) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        stopAnimation(player);
        
        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.isSleeping()) {
                    cancel();
                    return;
                }
                
                // Ultra-minimal sleep effects - only 1 particle every 2 seconds
                if (ticks % 40 == 0) {
                    createModernSleepEffect(player);
                }
                
                // Gentle sound every 4 seconds
                if (ticks % 80 == 0) {
                    playModernSleepSound(player);
                }
                
                ticks++;
                
                // Stop after 4 seconds
                if (ticks > 80) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        activeAnimations.put(player, task);
    }
    
    /**
     * Create modern sleep effect - single elegant particle
     */
    private void createModernSleepEffect(Player player) {
        Location loc = player.getLocation().add(0, 1.2, 0);
        
        // Single floating particle with gentle movement
        double x = (random.nextDouble() - 0.5) * 0.3;
        double z = (random.nextDouble() - 0.5) * 0.3;
        Location particleLoc = loc.clone().add(x, 0.2, z);
        
        // Modern particle choice based on config
        try {
            Particle sleepParticle = Particle.valueOf(plugin.getConfigManager().getSleepParticle());
            player.getWorld().spawnParticle(sleepParticle, particleLoc, 1, 0.02, 0.02, 0.02, 0.001);
        } catch (IllegalArgumentException e) {
            // Fallback to modern particle
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLoc, 1, 0.02, 0.02, 0.02, 0.001);
        }
    }
    
    /**
     * Play modern sleep sound - very gentle
     */
    private void playModernSleepSound(Player player) {
        if (!plugin.getConfigManager().areSoundEffectsEnabled()) {
            return;
        }
        
        float volume = (float) (0.08f * plugin.getConfigManager().getSoundVolume());
        try {
            Sound sleepSound = Sound.valueOf(plugin.getConfigManager().getSleepSound());
            player.playSound(player.getLocation(), sleepSound, volume, 1.9f);
        } catch (IllegalArgumentException e) {
            // Modern fallback sound
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, volume, 1.9f);
        }
    }
    
    /**
     * Modern night skip animation - clean and minimal
     */
    public void startNightSkipAnimation(World world) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        // Modern futuristic messages
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "&b⚡ &f&lNIGHT SKIP PROTOCOL &b&lACTIVATED &b⚡");
        MessageUtils.broadcastToWorld(world, "&7✨ &eTime acceleration in progress... &7✨");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "");
        
        // Start minimal animation
        startModernNightSkipSequence(world);
        
        // Individual player effects
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                startModernDreamEffect(player);
            } else {
                startModernAwakeEffect(player);
            }
        }
    }
    
    /**
     * Modern night skip sequence - ultra-minimal
     */
    private void startModernNightSkipSequence(World world) {
        new BukkitRunnable() {
            int phase = 0;
            int ticks = 0;
            
            @Override
            public void run() {
                if (world.getPlayers().isEmpty()) {
                    cancel();
                    return;
                }
                
                // Only animate for one random player to reduce server load
                Player randomPlayer = world.getPlayers().get(random.nextInt(world.getPlayers().size()));
                Location skyLoc = randomPlayer.getLocation().add(0, 8, 0);
                
                switch (phase) {
                    case 0: // Minimal time vortex
                        createModernTimeVortex(randomPlayer, skyLoc, ticks);
                        break;
                    case 1: // Simple completion effect
                        createModernCompletion(randomPlayer, skyLoc, ticks);
                        break;
                }
                
                ticks++;
                
                // Switch phase every 30 ticks (1.5 seconds)
                if (ticks >= 30) {
                    phase++;
                    ticks = 0;
                    
                    if (phase >= 2) {
                        // Modern completion message
                        MessageUtils.broadcastToWorld(world, "");
                        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                        MessageUtils.broadcastToWorld(world, "&a✓ &f&lNIGHT SKIP &a&lCOMPLETE &a✓");
                        MessageUtils.broadcastToWorld(world, "&7🌅 &eWelcome to the new dawn! &7🌅");
                        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                        MessageUtils.broadcastToWorld(world, "");
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Modern time vortex - single rotating particle
     */
    private void createModernTimeVortex(Player player, Location center, int ticks) {
        // Only 1 particle rotating slowly
        double angle = ticks * 0.2;
        double radius = 1.5;
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;
        double y = Math.sin(ticks * 0.1) * 0.3;
        
        Location vortexLoc = center.clone().add(x, y, z);
        
        // Modern particle
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, vortexLoc, 1, 0, 0, 0, 0);
        
        // Sound effect only once
        if (ticks == 0) {
            playModernTimeSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.15f, 1.5f);
        }
    }
    
    /**
     * Modern completion effect - simple burst
     */
    private void createModernCompletion(Player player, Location center, int ticks) {
        if (ticks == 0) {
            // Single burst of 3 particles
            for (int i = 0; i < 3; i++) {
                double angle = i * 120 * Math.PI / 180;
                double x = Math.cos(angle) * 0.8;
                double z = Math.sin(angle) * 0.8;
                Location burstLoc = center.clone().add(x, 0, z);
                
                player.getWorld().spawnParticle(Particle.TOTEM, burstLoc, 1, 0.1, 0.1, 0.1, 0.02);
            }
            
            // Completion sound
            playModernTimeSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.2f, 1.8f);
        }
    }
    
    /**
     * Modern dream effect for sleeping players
     */
    private void startModernDreamEffect(Player player) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                // Single gentle particle every 20 ticks
                if (ticks % 20 == 0) {
                    Location loc = player.getLocation().add(0, 1.5, 0);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.1, 0.1, 0.1, 0.001);
                }
                
                ticks++;
                if (ticks >= 60) { // 3 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 10L, 1L);
    }
    
    /**
     * Modern awake effect - very subtle
     */
    private void startModernAwakeEffect(Player player) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                // Single particle every 30 ticks
                if (ticks % 30 == 0) {
                    Location loc = player.getLocation().add(0, 1.2, 0);
                    double x = (random.nextDouble() - 0.5) * 0.4;
                    double z = (random.nextDouble() - 0.5) * 0.4;
                    Location particleLoc = loc.clone().add(x, 0, z);
                    
                    player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0.02, 0.02, 0.02, 0.001);
                }
                
                ticks++;
                if (ticks >= 40) { // 2 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 1L);
    }
    
    /**
     * Play modern time sound effects
     */
    private void playModernTimeSound(Player player, Sound sound, float volume, float pitch) {
        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
            float adjustedVolume = (float) (volume * plugin.getConfigManager().getSoundVolume());
            player.playSound(player.getLocation(), sound, adjustedVolume, pitch);
        }
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