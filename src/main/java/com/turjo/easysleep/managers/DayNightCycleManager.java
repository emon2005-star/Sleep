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
 * Epic Day-Night Cycle Animation System
 * Creates stunning futuristic visual effects during time acceleration
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
        startCycleMonitoring();
    }
    
    /**
     * Start monitoring for time acceleration events
     */
    private void startCycleMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getConfigManager().isDayNightAnimationEnabled()) {
                    return;
                }
                
                for (World world : plugin.getServer().getWorlds()) {
                    checkTimeAcceleration(world);
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Check every 0.5 seconds for accuracy
    }
    
    /**
     * Check for time acceleration in a world
     */
    private void checkTimeAcceleration(World world) {
        String worldName = world.getName();
        long currentTime = world.getTime();
        long lastTime = lastTimeCheck.getOrDefault(worldName, currentTime);
        
        // Detect rapid time change (time acceleration)
        long timeDiff = Math.abs(currentTime - lastTime);
        
        // If time jumped significantly (more than 100 ticks in 0.5 seconds)
        if (timeDiff > 100 && !animationActive.getOrDefault(worldName, false)) {
            triggerTimeAccelerationAnimation(world);
            animationActive.put(worldName, true);
            
            // Reset animation flag after 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    animationActive.put(worldName, false);
                }
            }.runTaskLater(plugin, 200L);
        }
        
        lastTimeCheck.put(worldName, currentTime);
    }
    
    /**
     * Trigger stunning futuristic time acceleration animation
     */
    private void triggerTimeAccelerationAnimation(World world) {
        // Skip if performance mode is enabled
        if (plugin.getConfigManager().isPerformanceMode()) {
            MessageUtils.broadcastToWorld(world, "&a✓ Night skipped! (Performance mode - reduced effects)");
            return;
        }
        
        if (plugin.getConfigManager().isDebugMode()) {
            plugin.getLogger().info("Triggering time acceleration animation in " + world.getName());
        }
        
        // Epic futuristic announcement
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "&b⚡ &f&lTIME ACCELERATION PROTOCOL &b&lACTIVATED &b⚡");
        MessageUtils.broadcastToWorld(world, "&7🌀 &eReality bending... Time streams converging... &7🌀");
        MessageUtils.broadcastToWorld(world, "&7⏰ &fTemporal status: &a&lACCELERATING &7⏰");
        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.broadcastToWorld(world, "");
        
        // Start the epic animation sequence
        startTimeAccelerationSequence(world);
    }
    
    /**
     * Epic time acceleration animation sequence
     */
    private void startTimeAccelerationSequence(World world) {
        new BukkitRunnable() {
            int phase = 0;
            int ticks = 0;
            
            @Override
            public void run() {
                // Performance check - only animate if players are online
                if (world.getPlayers().isEmpty()) {
                    cancel();
                    return;
                }
                
                for (Player player : world.getPlayers()) {
                    Location loc = player.getLocation().add(0, 3, 0);
                    
                    switch (phase) {
                        case 0: // Time Vortex Formation
                            createTimeVortex(player, loc, ticks);
                            if (ticks == 0) {
                                playTimeSound(player, Sound.BLOCK_PORTAL_AMBIENT, 0.3f, 0.8f);
                            }
                            break;
                            
                        case 1: // Reality Distortion
                            createRealityDistortion(player, loc, ticks);
                            if (ticks == 0) {
                                playTimeSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 0.4f, 1.5f);
                            }
                            break;
                            
                        case 2: // Time Stream Acceleration
                            createTimeStreamAcceleration(player, loc, ticks);
                            if (ticks == 0) {
                                playTimeSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 0.2f, 2.0f);
                            }
                            break;
                            
                        case 3: // Temporal Stabilization
                            createTemporalStabilization(player, loc, ticks);
                            if (ticks == 0) {
                                playTimeSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.5f, 1.8f);
                            }
                            break;
                    }
                }
                
                ticks++;
                
                // Move to next phase every 40 ticks (2 seconds)
                if (ticks >= 40) {
                    phase++;
                    ticks = 0;
                    
                    // End animation after all phases
                    if (phase >= 4) {
                        // Final announcement
                        MessageUtils.broadcastToWorld(world, "");
                        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        MessageUtils.broadcastToWorld(world, "&a✓ &f&lTIME ACCELERATION &a&lCOMPLETE &a✓");
                        MessageUtils.broadcastToWorld(world, "&7⚡ &eWelcome to the accelerated timeline! &7⚡");
                        MessageUtils.broadcastToWorld(world, "&7🌟 &fTemporal status: &a&lSTABILIZED &7🌟");
                        MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        MessageUtils.broadcastToWorld(world, "");
                        
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Create time vortex effect
     */
    private void createTimeVortex(Player player, Location center, int ticks) {
        World world = player.getWorld();
        double radius = 2.0 + Math.sin(ticks * 0.2) * 0.5;
        
        for (int i = 0; i < 12; i++) {
            double angle = (ticks * 0.3 + i * 30) * Math.PI / 180;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            double y = Math.sin(ticks * 0.1 + i) * 0.8;
            
            Location vortexLoc = center.clone().add(x, y, z);
            
            // Performance-friendly particle spawning
            if (isPlayerInRange(player, center, 32)) {
                try {
                    Particle particle = Particle.valueOf(plugin.getConfigManager().getAwakeParticle());
                    world.spawnParticle(particle, vortexLoc, 1, 0, 0, 0, 0);
                } catch (IllegalArgumentException e) {
                    world.spawnParticle(Particle.PORTAL, vortexLoc, 1, 0, 0, 0, 0);
                }
                
                if (plugin.getConfigManager().areEnhancedParticlesEnabled()) {
                    world.spawnParticle(Particle.END_ROD, vortexLoc, 1, 0.1, 0.1, 0.1, 0.01);
                }
            }
        }
    }
    
    /**
     * Create reality distortion effect
     */
    private void createRealityDistortion(Player player, Location center, int ticks) {
        World world = player.getWorld();
        
        // Expanding rings of distortion
        for (int ring = 1; ring <= 3; ring++) {
            double radius = ring * 1.5 + Math.sin(ticks * 0.15) * 0.3;
            
            for (int i = 0; i < 8 * ring; i++) {
                double angle = (ticks * 0.4 + i * (360.0 / (8 * ring))) * Math.PI / 180;
                double x = Math.cos(angle) * radius;
                double z = Math.sin(angle) * radius;
                double y = Math.sin(angle * 2 + ticks * 0.2) * 0.5;
                
                Location distortLoc = center.clone().add(x, y, z);
                
                if (isPlayerInRange(player, center, 32)) {
                    world.spawnParticle(Particle.REVERSE_PORTAL, distortLoc, 1, 0.1, 0.1, 0.1, 0.02);
                    
                    if (ring == 2) { // Middle ring gets special effects
                        world.spawnParticle(Particle.SPELL_WITCH, distortLoc, 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }
    
    /**
     * Create time stream acceleration effect
     */
    private void createTimeStreamAcceleration(Player player, Location center, int ticks) {
        World world = player.getWorld();
        
        // Vertical time streams
        for (int stream = 0; stream < 6; stream++) {
            double angle = stream * 60 * Math.PI / 180;
            double baseX = Math.cos(angle) * 2.5;
            double baseZ = Math.sin(angle) * 2.5;
            
            // Create ascending particles
            for (int height = 0; height < 8; height++) {
                double y = height * 0.5 - 2 + Math.sin(ticks * 0.3 + stream) * 0.3;
                double x = baseX + Math.sin(ticks * 0.2 + height) * 0.2;
                double z = baseZ + Math.cos(ticks * 0.2 + height) * 0.2;
                
                Location streamLoc = center.clone().add(x, y, z);
                
                if (isPlayerInRange(player, center, 32)) {
                    world.spawnParticle(Particle.DRAGON_BREATH, streamLoc, 1, 0.05, 0.05, 0.05, 0.01);
                    
                    if (height % 2 == 0) {
                        world.spawnParticle(Particle.ENCHANTMENT_TABLE, streamLoc, 1, 0.1, 0.1, 0.1, 0);
                    }
                }
            }
        }
    }
    
    /**
     * Create temporal stabilization effect
     */
    private void createTemporalStabilization(Player player, Location center, int ticks) {
        World world = player.getWorld();
        
        // Converging energy streams
        for (int i = 0; i < 20; i++) {
            double progress = (ticks + i * 2) / 40.0; // 0 to 1 over 40 ticks
            if (progress > 1.0) progress = 1.0;
            
            double angle = i * 18 * Math.PI / 180;
            double startRadius = 4.0;
            double currentRadius = startRadius * (1.0 - progress);
            
            double x = Math.cos(angle) * currentRadius;
            double z = Math.sin(angle) * currentRadius;
            double y = Math.sin(progress * Math.PI) * 1.5;
            
            Location stabilizeLoc = center.clone().add(x, y, z);
            
            if (isPlayerInRange(player, center, 32)) {
                world.spawnParticle(Particle.TOTEM, stabilizeLoc, 1, 0.1, 0.1, 0.1, 0.05);
                
                if (progress > 0.8) { // Final convergence
                    world.spawnParticle(Particle.FIREWORKS_SPARK, stabilizeLoc, 2, 0.2, 0.2, 0.2, 0.1);
                }
            }
        }
        
        // Central stabilization core
        if (ticks > 20) {
            world.spawnParticle(Particle.END_ROD, center, 3, 0.3, 0.3, 0.3, 0.05);
        }
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
    private boolean isPlayerInRange(Player player, Location center, int maxDistance) {
        if (plugin.getConfigManager().isPerformanceMode()) {
            maxDistance = Math.min(maxDistance, 16); // Reduce range in performance mode
        }
        
        for (Player p : player.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= maxDistance) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        lastTimeCheck.clear();
        animationActive.clear();
    }
}