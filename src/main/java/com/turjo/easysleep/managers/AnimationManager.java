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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Manages stunning night skip animations for sleeping and non-sleeping players
 * with optimized performance and configurable effects.
 * 
 * @author Turjo
 * @version 1.3.1
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
     * Start sleep animation for a player who is sleeping
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
                
                Location loc = player.getLocation().add(0, 1, 0);
                
                // Create dreamy particle effects
                if (ticks % 10 == 0) {
                    // Floating stars around sleeping player
                    for (int i = 0; i < 5; i++) {
                        double angle = (ticks + i * 72) * Math.PI / 180;
                        double x = Math.cos(angle) * 2;
                        double z = Math.sin(angle) * 2;
                        Location particleLoc = loc.clone().add(x, Math.sin(ticks * 0.1) * 0.5, z);
                        
                        try {
                            Particle sleepParticle = Particle.valueOf(plugin.getConfigManager().getSleepParticle());
                            player.getWorld().spawnParticle(sleepParticle, particleLoc, 1, 0, 0, 0, 0);
                        } catch (IllegalArgumentException e) {
                            player.getWorld().spawnParticle(Particle.HEART, particleLoc, 1, 0, 0, 0, 0);
                        }
                        
                        // Enhanced particles if enabled
                        if (plugin.getConfigManager().areEnhancedParticlesEnabled()) {
                            player.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0.1, 0.1, 0.1, 0.01);
                        }
                    }
                }
                
                // Gentle sound effects
                if (ticks % 40 == 0) {
                    if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                        float volume = (float) (0.2f * plugin.getConfigManager().getSoundVolume());
                        try {
                            Sound sleepSound = Sound.valueOf(plugin.getConfigManager().getSleepSound());
                            player.playSound(player.getLocation(), sleepSound, volume, 1.8f);
                        } catch (IllegalArgumentException e) {
                            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, volume, 1.8f);
                        }
                    }
                }
                
                // Dream particles
                if (ticks % 5 == 0) {
                    if (isPlayerInRange(player, loc)) {
                        int particleCount = (int) (1 * plugin.getConfigManager().getParticleDensity());
                        player.getWorld().spawnParticle(Particle.HEART, loc, particleCount, 0.5, 0.2, 0.5, 0.01);
                    }
                }
                
                ticks++;
                
                if (ticks > 600) { // Stop after 30 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        activeAnimations.put(player, task);
    }
    
    /**
     * Check if player is within animation range
     */
    private boolean isPlayerInRange(Player player, Location center) {
        int maxDistance = plugin.getConfigManager().getMaxAnimationDistance();
        
        // Reduce distance in performance mode
        if (plugin.getConfigManager().isPerformanceMode()) {
            maxDistance = Math.min(maxDistance, 16);
        }
        
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= maxDistance) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Start night skip animation for all players when night is being skipped
     */
    public void startNightSkipAnimation(World world) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        List<Player> sleepingPlayers = new ArrayList<>();
        List<Player> awakePlayers = new ArrayList<>();
        
        // Categorize players
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                sleepingPlayers.add(player);
            } else {
                awakePlayers.add(player);
            }
        }
        
        // Keep the original futuristic chat style you love
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "&b✦ &f&lNIGHT SKIP PROTOCOL &b&lACTIVATED &b✦");
        MessageUtils.broadcastToWorld(world, "&7⚡ &eDream sequence initiated... Reality bending... &7⚡");
        MessageUtils.broadcastToWorld(world, "&7🌙 &fTime acceleration: &a&lENGAGED &7🌙");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "");
        
        // Epic animation for sleeping players
        startEpicSleepAnimation(sleepingPlayers);
        
        // Mystical animation for awake players
        startMysticalAwakeAnimation(awakePlayers);
        
        // World-wide effects
        startWorldEffects(world);
    }
    
    /**
     * Epic animation for sleeping players during night skip
     */
    private void startEpicSleepAnimation(List<Player> players) {
        new BukkitRunnable() {
            int phase = 0;
            
            @Override
            public void run() {
                for (Player player : players) {
                    if (!player.isOnline()) continue;
                    
                    Location loc = player.getLocation().add(0, 1, 0);
                    
                    switch (phase) {
                        case 0: // Dream spiral
                            for (int i = 0; i < 8; i++) {
                                double angle = i * 45 * Math.PI / 180;
                                double radius = 1.5 + Math.sin(phase * 0.2) * 0.5;
                                double x = Math.cos(angle) * radius;
                                double z = Math.sin(angle) * radius;
                                Location spiralLoc = loc.clone().add(x, Math.sin(angle + phase * 0.3) * 0.8, z);
                                
                                player.getWorld().spawnParticle(Particle.END_ROD, spiralLoc, 1, 0, 0, 0, 0);
                                if (plugin.getConfigManager().areEnhancedParticlesEnabled()) {
                                    player.getWorld().spawnParticle(Particle.DRAGON_BREATH, spiralLoc, 2, 0.1, 0.1, 0.1, 0);
                                }
                            }
                            if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                                float volume = (float) (0.3f * plugin.getConfigManager().getSoundVolume());
                                player.playSound(loc, Sound.BLOCK_BEACON_AMBIENT, volume, 1.8f);
                            }
                            break;
                            
                        case 1: // Star burst
                            for (int i = 0; i < 12; i++) {
                                double angle = i * 30 * Math.PI / 180;
                                double distance = 2.5;
                                double x = Math.cos(angle) * distance;
                                double z = Math.sin(angle) * distance;
                                Location burstLoc = loc.clone().add(x, 1, z);
                                
                                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, burstLoc, 3, 0.2, 0.2, 0.2, 0.1);
                                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, burstLoc, 5, 0.3, 0.3, 0.3, 0);
                            }
                            if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                                float volume = (float) (0.4f * plugin.getConfigManager().getSoundVolume());
                                player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, volume, 1.5f);
                            }
                            break;
                            
                        case 2: // Dream completion
                            player.getWorld().spawnParticle(Particle.TOTEM, loc, 20, 1, 1, 1, 0.1);
                            player.getWorld().spawnParticle(Particle.END_ROD, loc, 15, 0.8, 0.8, 0.8, 0.05);
                            break;
                    }
                }
                
                phase++;
                if (phase >= 3) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    /**
     * Mystical animation for awake players during night skip
     */
    private void startMysticalAwakeAnimation(List<Player> players) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                for (Player player : players) {
                    if (!player.isOnline()) continue;
                    
                    Location loc = player.getLocation().add(0, 1, 0);
                    
                    // Mystical aura around awake players
                    if (ticks % 5 == 0) {
                        double angle = ticks * 0.2;
                        for (int i = 0; i < 6; i++) {
                            double currentAngle = angle + (i * 60 * Math.PI / 180);
                            double x = Math.cos(currentAngle) * 1.2;
                            double z = Math.sin(currentAngle) * 1.2;
                            Location auraLoc = loc.clone().add(x, Math.sin(currentAngle * 2) * 0.3, z);
                            
                            player.getWorld().spawnParticle(Particle.PORTAL, auraLoc, 2, 0.1, 0.1, 0.1, 0);
                            player.getWorld().spawnParticle(Particle.SPELL_WITCH, auraLoc, 1, 0, 0, 0, 0);
                        }
                    }
                    
                    // Time distortion effects
                    if (ticks % 15 == 0) {
                        player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc, 8, 0.5, 0.5, 0.5, 0.02);
                        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                            float volume = (float) (0.2f * plugin.getConfigManager().getSoundVolume());
                            player.playSound(loc, Sound.BLOCK_PORTAL_AMBIENT, volume, 1.8f);
                        }
                    }
                    
                    // Send mystical message
                    if (ticks == 20) {
                        MessageUtils.sendMessage(player, "&5✧ &f&lTIME DISTORTION &c&lDETECTED &5✧");
                        MessageUtils.sendMessage(player, "&7Reality bends around the &edreaming souls&7...");
                        MessageUtils.sendMessage(player, "&7🔮 &fMystic energy: &d&lFLOWING &7🔮");
                    }
                }
                
                ticks++;
                if (ticks >= 60) { // 3 seconds
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * World-wide effects during night skip
     */
    private void startWorldEffects(World world) {
        new BukkitRunnable() {
            int phase = 0;
            
            @Override
            public void run() {
                // Lightning effects (visual only)
                if (phase % 10 == 0) {
                    for (Player player : world.getPlayers()) {
                        if (random.nextDouble() < 0.3) {
                            Location strikeLoc = player.getLocation().add(
                                random.nextInt(20) - 10, 
                                0, 
                                random.nextInt(20) - 10
                            );
                            world.strikeLightningEffect(strikeLoc);
                        }
                    }
                }
                
                // Global sound effects
                if (phase == 0) {
                    for (Player player : world.getPlayers()) {
                        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                            float volume = (float) (0.2f * plugin.getConfigManager().getSoundVolume());
                            // Replaced dragon roar with more relaxing sound
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, volume, 0.8f);
                        }
                    }
                } else if (phase == 30) {
                    for (Player player : world.getPlayers()) {
                        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                            float volume = (float) (0.15f * plugin.getConfigManager().getSoundVolume());
                            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, volume, 2.0f);
                        }
                    }
                } else if (phase == 60) {
                    for (Player player : world.getPlayers()) {
                        if (plugin.getConfigManager().areSoundEffectsEnabled()) {
                            float volume = (float) (0.4f * plugin.getConfigManager().getSoundVolume());
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, volume, 1.2f);
                        }
                    }
                    
                    // Final announcement
                    MessageUtils.broadcastToWorld(world, "");
                    MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    MessageUtils.broadcastToWorld(world, "&a✓ &f&lNIGHT SKIP PROTOCOL &a&lCOMPLETE &a✓");
                    MessageUtils.broadcastToWorld(world, "&7⚡ &eWelcome to a new dawn, time travelers! &7⚡");
                    MessageUtils.broadcastToWorld(world, "&7🌅 &fReality status: &a&lSTABILIZED &7🌅");
                    MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    MessageUtils.broadcastToWorld(world, "");
                }
                
                phase++;
                if (phase >= 80) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
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