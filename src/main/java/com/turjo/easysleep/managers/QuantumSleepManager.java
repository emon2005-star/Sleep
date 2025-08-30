package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 🌌 QUANTUM SLEEP MANAGER - WORLD'S FIRST QUANTUM SLEEP SYSTEM 🌌
 * Revolutionary quantum entanglement between sleeping players across dimensions
 * 
 * @author Turjo
 * @version 1.5.2
 */
public class QuantumSleepManager {
    
    private final EasySleep plugin;
    private final Map<UUID, QuantumState> quantumStates;
    private final Map<String, Set<UUID>> quantumNetworks;
    private final Random random;
    
    // Quantum sleep states
    private enum QuantumState {
        ENTANGLED, SUPERPOSITION, COLLAPSED, COHERENT, DECOHERENT
    }
    
    public QuantumSleepManager(EasySleep plugin) {
        this.plugin = plugin;
        this.quantumStates = new HashMap<>();
        this.quantumNetworks = new HashMap<>();
        this.random = new Random();
        startQuantumMonitoring();
    }
    
    /**
     * 🌟 Start quantum sleep monitoring across all dimensions
     */
    private void startQuantumMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkQuantumEntanglement();
                maintainQuantumCoherence();
            }
        }.runTaskTimer(plugin, 0L, 40L); // Check every 2 seconds
    }
    
    /**
     * 🌟 Check for quantum entanglement between sleeping players
     */
    private void checkQuantumEntanglement() {
        Map<String, List<Player>> worldSleepers = new HashMap<>();
        
        // Collect sleeping players from all worlds
        for (World world : plugin.getServer().getWorlds()) {
            List<Player> sleepers = new ArrayList<>();
            for (Player player : world.getPlayers()) {
                if (player.isSleeping() && !plugin.getAFKManager().isPlayerAFK(player)) {
                    sleepers.add(player);
                }
            }
            if (!sleepers.isEmpty()) {
                worldSleepers.put(world.getName(), sleepers);
            }
        }
        
        // Create quantum entanglement if multiple worlds have sleepers
        if (worldSleepers.size() >= 2) {
            createQuantumEntanglement(worldSleepers);
        }
    }
    
    /**
     * 🌟 Create quantum entanglement between dimensions
     */
    private void createQuantumEntanglement(Map<String, List<Player>> worldSleepers) {
        String networkId = "quantum_" + System.currentTimeMillis();
        Set<UUID> entangledPlayers = new HashSet<>();
        
        // Entangle all sleeping players across dimensions
        for (List<Player> sleepers : worldSleepers.values()) {
            for (Player player : sleepers) {
                UUID uuid = player.getUniqueId();
                quantumStates.put(uuid, QuantumState.ENTANGLED);
                entangledPlayers.add(uuid);
            }
        }
        
        quantumNetworks.put(networkId, entangledPlayers);
        
        // Announce quantum entanglement
        for (List<Player> sleepers : worldSleepers.values()) {
            World world = sleepers.get(0).getWorld();
            MessageUtils.broadcastToWorld(world, "");
            MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world, "&b⚛ &f&lQUANTUM ENTANGLEMENT &b&lDETECTED &b⚛");
            MessageUtils.broadcastToWorld(world, "&7🌌 &fCross-dimensional sleep synchronization &bactivated");
            MessageUtils.broadcastToWorld(world, "&7⚡ &fEntangled players: &e" + entangledPlayers.size() + " &7across &e" + worldSleepers.size() + " &7dimensions");
            MessageUtils.broadcastToWorld(world, "&7💫 &fQuantum coherence &destablishing&f...");
            MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world, "");
        }
        
        // Start quantum effects
        startQuantumEffects(networkId, worldSleepers);
    }
    
    /**
     * 🌟 Start quantum effects across dimensions
     */
    private void startQuantumEffects(String networkId, Map<String, List<Player>> worldSleepers) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                Set<UUID> network = quantumNetworks.get(networkId);
                if (network == null || network.isEmpty()) {
                    cancel();
                    return;
                }
                
                // Check if any players are still sleeping
                boolean anyStillSleeping = false;
                for (UUID uuid : network) {
                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player != null && player.isOnline() && player.isSleeping()) {
                        anyStillSleeping = true;
                        break;
                    }
                }
                
                if (!anyStillSleeping) {
                    endQuantumEntanglement(networkId);
                    cancel();
                    return;
                }
                
                // Create quantum effects
                for (List<Player> sleepers : worldSleepers.values()) {
                    for (Player player : sleepers) {
                        if (player.isOnline() && player.isSleeping()) {
                            createQuantumParticles(player, ticks);
                        }
                    }
                }
                
                // Quantum synchronization pulse every 3 seconds
                if (ticks % 60 == 0) {
                    createQuantumSynchronization(worldSleepers);
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * 🌟 Create quantum particles around entangled players
     */
    private void createQuantumParticles(Player player, int ticks) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        // Quantum field particles
        for (int i = 0; i < 2; i++) {
            double angle = (ticks * 0.1) + (i * 180);
            double radius = 0.8 + Math.sin(ticks * 0.05) * 0.2;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.08 + i) * 0.3;
            
            Location quantumLoc = loc.clone().add(x, y, z);
            
            // Quantum particles (alternating colors)
            Particle quantumParticle = (i % 2 == 0) ? Particle.END_ROD : Particle.SOUL_FIRE_FLAME;
            player.getWorld().spawnParticle(quantumParticle, quantumLoc, 1, 0.05, 0.05, 0.05, 0.01);
        }
        
        // Quantum probability cloud every 2 seconds
        if (ticks % 40 == 0) {
            player.getWorld().spawnParticle(Particle.PORTAL, loc, 3, 0.5, 0.5, 0.5, 0.02);
        }
    }
    
    /**
     * 🌟 Create quantum synchronization pulse
     */
    private void createQuantumSynchronization(Map<String, List<Player>> worldSleepers) {
        for (List<Player> sleepers : worldSleepers.values()) {
            if (!sleepers.isEmpty()) {
                World world = sleepers.get(0).getWorld();
                
                // Find center point of sleeping players in this world
                Location center = calculateWorldCenter(sleepers);
                
                // Quantum pulse effect
                world.spawnParticle(Particle.TOTEM, center, 5, 2.0, 2.0, 2.0, 0.1);
                world.playSound(center, Sound.BLOCK_BEACON_POWER_SELECT, 0.2f, 2.0f);
                
                // Broadcast quantum sync message
                MessageUtils.broadcastToWorld(world, "&7⚛ &bQuantum synchronization pulse &7⚛");
            }
        }
    }
    
    /**
     * 🌟 Calculate center point of players in world
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
            (totalY / players.size()) + 3.0,
            totalZ / players.size()
        );
    }
    
    /**
     * 🌟 End quantum entanglement
     */
    private void endQuantumEntanglement(String networkId) {
        Set<UUID> network = quantumNetworks.remove(networkId);
        if (network == null) return;
        
        // Clear quantum states
        for (UUID uuid : network) {
            quantumStates.remove(uuid);
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                // Quantum decoherence message
                MessageUtils.sendMessage(player, "&7⚛ &cQuantum entanglement &7collapsed - returning to classical reality &7⚛");
                
                // Decoherence effect
                Location loc = player.getLocation().add(0, 1.5, 0);
                player.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.5, 0.5, 0.5, 0.05);
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.3f, 0.8f);
            }
        }
    }
    
    /**
     * 🌟 Maintain quantum coherence
     */
    private void maintainQuantumCoherence() {
        // Clean up disconnected players from quantum networks
        for (String networkId : new ArrayList<>(quantumNetworks.keySet())) {
            Set<UUID> network = quantumNetworks.get(networkId);
            network.removeIf(uuid -> {
                Player player = plugin.getServer().getPlayer(uuid);
                return player == null || !player.isOnline() || !player.isSleeping();
            });
            
            if (network.isEmpty()) {
                quantumNetworks.remove(networkId);
            }
        }
    }
    
    /**
     * Check if player is in quantum state
     */
    public boolean isQuantumEntangled(Player player) {
        return quantumStates.containsKey(player.getUniqueId());
    }
    
    /**
     * Get active quantum networks count
     */
    public int getActiveQuantumNetworks() {
        return quantumNetworks.size();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        quantumStates.clear();
        quantumNetworks.clear();
    }
}