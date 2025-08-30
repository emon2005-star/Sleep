package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 🌌 DIMENSIONAL SLEEP MANAGER - EXCLUSIVE CROSS-WORLD SLEEP SYSTEM 🌌
 * Revolutionary dimensional sleep synchronization with portal effects
 * 
 * @author Turjo
 * @version 1.5.2
 */
public class DimensionalSleepManager {
    
    private final EasySleep plugin;
    private final Map<String, DimensionalPortal> activePortals;
    private final Map<UUID, String> playerDimensions;
    private final Random random;
    
    // Portal types for different world combinations
    private enum PortalType {
        OVERWORLD_NETHER, OVERWORLD_END, NETHER_END, CUSTOM_DIMENSION
    }
    
    public DimensionalSleepManager(EasySleep plugin) {
        this.plugin = plugin;
        this.activePortals = new HashMap<>();
        this.playerDimensions = new HashMap<>();
        this.random = new Random();
        startDimensionalMonitoring();
    }
    
    /**
     * 🌟 Start dimensional sleep monitoring
     */
    private void startDimensionalMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkDimensionalSleep();
                maintainPortals();
            }
        }.runTaskTimer(plugin, 0L, 60L); // Check every 3 seconds
    }
    
    /**
     * 🌟 Check for dimensional sleep synchronization
     */
    private void checkDimensionalSleep() {
        Map<World.Environment, List<Player>> dimensionSleepers = new HashMap<>();
        
        // Collect sleeping players by dimension type
        for (World world : plugin.getServer().getWorlds()) {
            List<Player> sleepers = new ArrayList<>();
            for (Player player : world.getPlayers()) {
                if (player.isSleeping() && !plugin.getAFKManager().isPlayerAFK(player)) {
                    sleepers.add(player);
                }
            }
            
            if (!sleepers.isEmpty()) {
                dimensionSleepers.put(world.getEnvironment(), sleepers);
            }
        }
        
        // Create dimensional portals if multiple dimensions have sleepers
        if (dimensionSleepers.size() >= 2) {
            createDimensionalPortals(dimensionSleepers);
        }
    }
    
    /**
     * 🌟 Create dimensional portals between sleeping dimensions
     */
    private void createDimensionalPortals(Map<World.Environment, List<Player>> dimensionSleepers) {
        List<World.Environment> dimensions = new ArrayList<>(dimensionSleepers.keySet());
        
        for (int i = 0; i < dimensions.size(); i++) {
            for (int j = i + 1; j < dimensions.size(); j++) {
                World.Environment dim1 = dimensions.get(i);
                World.Environment dim2 = dimensions.get(j);
                
                String portalId = dim1.name() + "_" + dim2.name();
                
                if (!activePortals.containsKey(portalId)) {
                    PortalType portalType = determinePortalType(dim1, dim2);
                    DimensionalPortal portal = new DimensionalPortal(portalType, dim1, dim2, System.currentTimeMillis());
                    activePortals.put(portalId, portal);
                    
                    // Announce portal creation
                    announceDimensionalPortal(dimensionSleepers.get(dim1), dimensionSleepers.get(dim2), portalType);
                    
                    // Start portal effects
                    startPortalEffects(portalId, dimensionSleepers.get(dim1), dimensionSleepers.get(dim2));
                }
            }
        }
    }
    
    /**
     * 🌟 Determine portal type based on dimensions
     */
    private PortalType determinePortalType(World.Environment dim1, World.Environment dim2) {
        if ((dim1 == World.Environment.NORMAL && dim2 == World.Environment.NETHER) ||
            (dim1 == World.Environment.NETHER && dim2 == World.Environment.NORMAL)) {
            return PortalType.OVERWORLD_NETHER;
        } else if ((dim1 == World.Environment.NORMAL && dim2 == World.Environment.THE_END) ||
                   (dim1 == World.Environment.THE_END && dim2 == World.Environment.NORMAL)) {
            return PortalType.OVERWORLD_END;
        } else if ((dim1 == World.Environment.NETHER && dim2 == World.Environment.THE_END) ||
                   (dim1 == World.Environment.THE_END && dim2 == World.Environment.NETHER)) {
            return PortalType.NETHER_END;
        } else {
            return PortalType.CUSTOM_DIMENSION;
        }
    }
    
    /**
     * 🌟 Announce dimensional portal creation
     */
    private void announceDimensionalPortal(List<Player> sleepers1, List<Player> sleepers2, PortalType portalType) {
        String portalName = getPortalName(portalType);
        
        // Announce to first dimension
        if (!sleepers1.isEmpty()) {
            World world1 = sleepers1.get(0).getWorld();
            MessageUtils.broadcastToWorld(world1, "");
            MessageUtils.broadcastToWorld(world1, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world1, "&5🌌 &f&lDIMENSIONAL PORTAL &5&lOPENED &5🌌");
            MessageUtils.broadcastToWorld(world1, "&7✨ &fPortal Type: &d" + portalName);
            MessageUtils.broadcastToWorld(world1, "&7🌟 &fConnected Dreamers: &e" + (sleepers1.size() + sleepers2.size()));
            MessageUtils.broadcastToWorld(world1, "&7💫 &fDimensional bridge &bstabilizing&f...");
            MessageUtils.broadcastToWorld(world1, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world1, "");
        }
        
        // Announce to second dimension
        if (!sleepers2.isEmpty()) {
            World world2 = sleepers2.get(0).getWorld();
            MessageUtils.broadcastToWorld(world2, "");
            MessageUtils.broadcastToWorld(world2, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world2, "&5🌌 &f&lDIMENSIONAL PORTAL &5&lOPENED &5🌌");
            MessageUtils.broadcastToWorld(world2, "&7✨ &fPortal Type: &d" + portalName);
            MessageUtils.broadcastToWorld(world2, "&7🌟 &fConnected Dreamers: &e" + (sleepers1.size() + sleepers2.size()));
            MessageUtils.broadcastToWorld(world2, "&7💫 &fDimensional bridge &bstabilizing&f...");
            MessageUtils.broadcastToWorld(world2, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world2, "");
        }
    }
    
    /**
     * 🌟 Start portal effects between dimensions
     */
    private void startPortalEffects(String portalId, List<Player> sleepers1, List<Player> sleepers2) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!activePortals.containsKey(portalId)) {
                    cancel();
                    return;
                }
                
                // Create portal effects in both dimensions
                createPortalEffectsInDimension(sleepers1, ticks);
                createPortalEffectsInDimension(sleepers2, ticks);
                
                // Dimensional resonance every 4 seconds
                if (ticks % 80 == 0) {
                    createDimensionalResonance(sleepers1, sleepers2);
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * 🌟 Create portal effects in a dimension
     */
    private void createPortalEffectsInDimension(List<Player> sleepers, int ticks) {
        if (sleepers.isEmpty()) return;
        
        Location center = calculateWorldCenter(sleepers);
        
        // Portal ring effect
        for (int i = 0; i < 8; i++) {
            double angle = (ticks * 0.2) + (i * 45);
            double radius = 3.0 + Math.sin(ticks * 0.05) * 0.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.08 + i) * 0.8;
            
            Location portalLoc = center.clone().add(x, y, z);
            
            // Portal particles
            Particle portalParticle = (i % 2 == 0) ? Particle.PORTAL : Particle.END_ROD;
            center.getWorld().spawnParticle(portalParticle, portalLoc, 1, 0.1, 0.1, 0.1, 0.02);
        }
        
        // Central portal energy
        if (ticks % 30 == 0) {
            center.getWorld().spawnParticle(Particle.DRAGON_BREATH, center, 3, 1.0, 1.0, 1.0, 0.05);
        }
    }
    
    /**
     * 🌟 Create dimensional resonance between portals
     */
    private void createDimensionalResonance(List<Player> sleepers1, List<Player> sleepers2) {
        if (sleepers1.isEmpty() || sleepers2.isEmpty()) return;
        
        Location center1 = calculateWorldCenter(sleepers1);
        Location center2 = calculateWorldCenter(sleepers2);
        
        // Resonance effects
        center1.getWorld().spawnParticle(Particle.TOTEM, center1, 8, 2.0, 2.0, 2.0, 0.15);
        center2.getWorld().spawnParticle(Particle.TOTEM, center2, 8, 2.0, 2.0, 2.0, 0.15);
        
        // Resonance sounds
        center1.getWorld().playSound(center1, Sound.BLOCK_BEACON_POWER_SELECT, 0.3f, 1.8f);
        center2.getWorld().playSound(center2, Sound.BLOCK_BEACON_POWER_SELECT, 0.3f, 1.8f);
        
        // Broadcast resonance message
        MessageUtils.broadcastToWorld(center1.getWorld(), "&7🌌 &bDimensional resonance &7detected - &dstabilizing portal matrix&7... &7🌌");
        MessageUtils.broadcastToWorld(center2.getWorld(), "&7🌌 &bDimensional resonance &7detected - &dstabilizing portal matrix&7... &7🌌");
    }
    
    /**
     * Calculate center point of players in world
     */
    private Location calculateWorldCenter(List<Player> players) {
        double totalX = 0, totalY = 0, totalZ = 0;
        for (Player player : players) {
            Location loc = player.getLocation();
            totalX += loc.getX();
            totalY += loc.getY();
            totalZ += loc.getZ();
        }
        
        return new Location(
            players.get(0).getWorld(),
            totalX / players.size(),
            (totalY / players.size()) + 5.0,
            totalZ / players.size()
        );
    }
    
    /**
     * Get portal name for display
     */
    private String getPortalName(PortalType portalType) {
        switch (portalType) {
            case OVERWORLD_NETHER: return "Overworld ↔ Nether Bridge";
            case OVERWORLD_END: return "Overworld ↔ End Void Gateway";
            case NETHER_END: return "Nether ↔ End Chaos Portal";
            case CUSTOM_DIMENSION: return "Custom Dimensional Gateway";
            default: return "Unknown Portal";
        }
    }
    
    /**
     * Maintain active portals
     */
    private void maintainPortals() {
        // Clean up inactive portals
        activePortals.entrySet().removeIf(entry -> {
            long duration = System.currentTimeMillis() - entry.getValue().creationTime;
            return duration > 300000; // Remove portals after 5 minutes
        });
    }
    
    /**
     * Get active portal count
     */
    public int getActivePortalCount() {
        return activePortals.size();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        activePortals.clear();
        playerDimensions.clear();
    }
    
    /**
     * Dimensional portal data class
     */
    private static class DimensionalPortal {
        final PortalType portalType;
        final World.Environment dimension1;
        final World.Environment dimension2;
        final long creationTime;
        
        DimensionalPortal(PortalType portalType, World.Environment dimension1, World.Environment dimension2, long creationTime) {
            this.portalType = portalType;
            this.dimension1 = dimension1;
            this.dimension2 = dimension2;
            this.creationTime = creationTime;
        }
    }
}