package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 🔮 EXCLUSIVE SLEEP RITUAL MANAGER 🔮
 * Revolutionary group sleep rituals with stunning collective effects
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class SleepRitualManager {
    
    private final EasySleep plugin;
    private final Map<String, SleepRitual> activeRituals;
    private final Random random;
    
    // Ritual types with unique collective effects
    private enum RitualType {
        HARMONY_CIRCLE, DREAM_CONVERGENCE, ASTRAL_PROJECTION,
        TEMPORAL_SYNC, SOUL_BINDING, COSMIC_ALIGNMENT
    }
    
    public SleepRitualManager(EasySleep plugin) {
        this.plugin = plugin;
        this.activeRituals = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * 🌟 Check and potentially trigger sleep ritual
     */
    public void checkSleepRitual(World world) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        List<Player> sleepingPlayers = new ArrayList<>();
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                sleepingPlayers.add(player);
            }
        }
        
        String worldName = world.getName();
        
        // Trigger ritual based on sleeping player count
        if (sleepingPlayers.size() >= 2 && !activeRituals.containsKey(worldName)) {
            RitualType ritualType = determineRitualType(sleepingPlayers.size());
            startSleepRitual(world, sleepingPlayers, ritualType);
        } else if (sleepingPlayers.size() < 2 && activeRituals.containsKey(worldName)) {
            endSleepRitual(world);
        }
    }
    
    /**
     * 🌟 Determine ritual type based on participant count
     */
    private RitualType determineRitualType(int playerCount) {
        if (playerCount >= 6) return RitualType.COSMIC_ALIGNMENT;
        if (playerCount >= 5) return RitualType.SOUL_BINDING;
        if (playerCount >= 4) return RitualType.TEMPORAL_SYNC;
        if (playerCount >= 3) return RitualType.ASTRAL_PROJECTION;
        if (playerCount >= 2) return RitualType.DREAM_CONVERGENCE;
        return RitualType.HARMONY_CIRCLE;
    }
    
    /**
     * 🌟 Start exclusive sleep ritual
     */
    private void startSleepRitual(World world, List<Player> participants, RitualType ritualType) {
        String worldName = world.getName();
        SleepRitual ritual = new SleepRitual(ritualType, participants, System.currentTimeMillis());
        activeRituals.put(worldName, ritual);
        
        // Calculate ritual center point
        Location center = calculateRitualCenter(participants);
        
        // Announce ritual activation
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "&5🔮 &f&lSLEEP RITUAL &5&lACTIVATED &5🔮");
        MessageUtils.broadcastToWorld(world, "&7✨ &fRitual Type: &d" + getRitualName(ritualType));
        MessageUtils.broadcastToWorld(world, "&7👥 &fParticipants: &e" + participants.size() + " &7dreamers");
        MessageUtils.broadcastToWorld(world, "&7🌟 &fCollective dream energy &bconverging&f...");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "");
        
        // Start ritual sequence
        startRitualSequence(world, ritual, center);
        
        // Individual participant effects
        for (Player participant : participants) {
            startParticipantEffects(participant, ritualType);
        }
        
        // Ritual completion sound
        world.playSound(center, Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 1.5f);
    }
    
    /**
     * 🌟 Calculate ritual center point
     */
    private Location calculateRitualCenter(List<Player> participants) {
        double totalX = 0, totalY = 0, totalZ = 0;
        for (Player player : participants) {
            Location loc = player.getLocation();
            totalX += loc.getX();
            totalY += loc.getY();
            totalZ += loc.getZ();
        }
        
        return new Location(
            participants.get(0).getWorld(),
            totalX / participants.size(),
            (totalY / participants.size()) + 3.0, // Elevated center
            totalZ / participants.size()
        );
    }
    
    /**
     * 🌟 Start ritual sequence
     */
    private void startRitualSequence(World world, SleepRitual ritual, Location center) {
        new BukkitRunnable() {
            int ticks = 0;
            int phase = 0;
            
            @Override
            public void run() {
                if (!activeRituals.containsKey(world.getName())) {
                    cancel();
                    return;
                }
                
                switch (phase) {
                    case 0: // Ritual formation (0-4 seconds)
                        createRitualFormation(world, ritual, center, ticks);
                        if (ticks >= 80) { phase++; ticks = 0; }
                        break;
                    case 1: // Energy convergence (4-8 seconds)
                        createEnergyConvergence(world, ritual, center, ticks);
                        if (ticks >= 80) { phase++; ticks = 0; }
                        break;
                    case 2: // Ritual climax (8-12 seconds)
                        createRitualClimax(world, ritual, center, ticks);
                        if (ticks >= 80) { phase++; ticks = 0; }
                        break;
                    case 3: // Sustained ritual state
                        createSustainedRitual(world, ritual, center, ticks);
                        break;
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * 🌟 Create ritual formation
     */
    private void createRitualFormation(World world, SleepRitual ritual, Location center, int ticks) {
        int participantCount = ritual.participants.size();
        
        // Create ritual circle
        for (int i = 0; i < participantCount; i++) {
            double angle = (360.0 / participantCount) * i + (ticks * 0.5);
            double radius = 3.0 + Math.sin(ticks * 0.05) * 0.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.08 + i) * 0.3;
            
            Location ritualLoc = center.clone().add(x, y, z);
            
            // Ritual-specific particles
            switch (ritual.ritualType) {
                case HARMONY_CIRCLE:
                    world.spawnParticle(Particle.HEART, ritualLoc, 1, 0.1, 0.1, 0.1, 0.01);
                    break;
                case DREAM_CONVERGENCE:
                    world.spawnParticle(Particle.SOUL_FIRE_FLAME, ritualLoc, 1, 0.1, 0.1, 0.1, 0.01);
                    break;
                case ASTRAL_PROJECTION:
                    world.spawnParticle(Particle.END_ROD, ritualLoc, 1, 0.1, 0.1, 0.1, 0.02);
                    break;
                case TEMPORAL_SYNC:
                    world.spawnParticle(Particle.PORTAL, ritualLoc, 2, 0.1, 0.1, 0.1, 0.03);
                    break;
                case SOUL_BINDING:
                    world.spawnParticle(Particle.SOUL, ritualLoc, 1, 0.1, 0.1, 0.1, 0.01);
                    break;
                case COSMIC_ALIGNMENT:
                    world.spawnParticle(Particle.TOTEM, ritualLoc, 1, 0.1, 0.1, 0.1, 0.02);
                    break;
            }
        }
        
        // Central ritual energy
        if (ticks % 20 == 0) {
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, center, 5, 0.5, 0.5, 0.5, 0.1);
        }
    }
    
    /**
     * 🌟 Create energy convergence
     */
    private void createEnergyConvergence(World world, SleepRitual ritual, Location center, int ticks) {
        // Energy streams from participants to center
        for (int i = 0; i < ritual.participants.size(); i++) {
            Player participant = ritual.participants.get(i);
            if (!participant.isOnline() || !participant.isSleeping()) continue;
            
            Location playerLoc = participant.getLocation().add(0, 1.5, 0);
            
            // Create energy stream
            for (int j = 0; j < 3; j++) {
                double progress = j / 3.0;
                Location streamLoc = playerLoc.clone().add(
                    (center.getX() - playerLoc.getX()) * progress,
                    (center.getY() - playerLoc.getY()) * progress,
                    (center.getZ() - playerLoc.getZ()) * progress
                );
                
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, streamLoc, 1, 0.05, 0.05, 0.05, 0.02);
            }
        }
        
        // Convergence message
        if (ticks == 40) {
            MessageUtils.broadcastToWorld(world, "&7🌟 &fDream energies &bconverging&f... ritual power &dincreasing&f... &7🌟");
        }
    }
    
    /**
     * 🌟 Create ritual climax
     */
    private void createRitualClimax(World world, SleepRitual ritual, Location center, int ticks) {
        // Explosive ritual energy
        if (ticks % 10 == 0) {
            for (int i = 0; i < 8; i++) {
                double angle = i * 45 + (ticks * 2);
                double radius = 2.0 + Math.sin(ticks * 0.1) * 0.8;
                double x = Math.cos(Math.toRadians(angle)) * radius;
                double z = Math.sin(Math.toRadians(angle)) * radius;
                double y = Math.sin(ticks * 0.15 + i) * 1.0;
                
                Location climaxLoc = center.clone().add(x, y, z);
                world.spawnParticle(Particle.TOTEM, climaxLoc, 2, 0.2, 0.2, 0.2, 0.05);
            }
        }
        
        // Climax sound and message
        if (ticks == 40) {
            MessageUtils.broadcastToWorld(world, "&7⚡ &fRitual &dclimax &freached! &bCollective consciousness &factivated! &7⚡");
            world.playSound(center, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.3f, 2.0f);
        }
    }
    
    /**
     * 🌟 Create sustained ritual state
     */
    private void createSustainedRitual(World world, SleepRitual ritual, Location center, int ticks) {
        // Gentle sustained effects every 2 seconds
        if (ticks % 40 == 0) {
            // Pulsing ritual energy
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, center, 3, 1.0, 1.0, 1.0, 0.02);
            
            // Soft ritual sound
            world.playSound(center, Sound.BLOCK_BEACON_AMBIENT, 0.1f, 1.8f);
        }
        
        // Participant connection lines every 4 seconds
        if (ticks % 80 == 0) {
            for (Player participant : ritual.participants) {
                if (participant.isOnline() && participant.isSleeping()) {
                    Location playerLoc = participant.getLocation().add(0, 1.5, 0);
                    world.spawnParticle(Particle.END_ROD, playerLoc, 1, 0.2, 0.2, 0.2, 0.01);
                }
            }
        }
    }
    
    /**
     * 🌟 Start participant effects
     */
    private void startParticipantEffects(Player participant, RitualType ritualType) {
        new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (!participant.isOnline() || !participant.isSleeping()) {
                    cancel();
                    return;
                }
                
                Location loc = participant.getLocation().add(0, 1.2, 0);
                
                // Ritual-specific participant effects
                switch (ritualType) {
                    case HARMONY_CIRCLE:
                        if (ticks % 30 == 0) {
                            participant.getWorld().spawnParticle(Particle.HEART, loc, 1, 0.3, 0.3, 0.3, 0.01);
                        }
                        break;
                    case DREAM_CONVERGENCE:
                        if (ticks % 25 == 0) {
                            participant.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 2, 0.2, 0.2, 0.2, 0.02);
                        }
                        break;
                    case ASTRAL_PROJECTION:
                        if (ticks % 20 == 0) {
                            participant.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0.4, 0.4, 0.4, 0.03);
                        }
                        break;
                    case TEMPORAL_SYNC:
                        if (ticks % 35 == 0) {
                            participant.getWorld().spawnParticle(Particle.PORTAL, loc, 3, 0.3, 0.3, 0.3, 0.05);
                        }
                        break;
                    case SOUL_BINDING:
                        if (ticks % 40 == 0) {
                            participant.getWorld().spawnParticle(Particle.SOUL, loc, 2, 0.2, 0.2, 0.2, 0.02);
                        }
                        break;
                    case COSMIC_ALIGNMENT:
                        if (ticks % 15 == 0) {
                            participant.getWorld().spawnParticle(Particle.TOTEM, loc, 1, 0.5, 0.5, 0.5, 0.04);
                        }
                        break;
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 20L, 1L);
    }
    
    /**
     * 🌟 End sleep ritual
     */
    private void endSleepRitual(World world) {
        String worldName = world.getName();
        SleepRitual ritual = activeRituals.remove(worldName);
        
        if (ritual != null) {
            long duration = (System.currentTimeMillis() - ritual.startTime) / 1000;
            
            // Ritual completion message
            MessageUtils.broadcastToWorld(world, "");
            MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world, "&a✓ &f&lSLEEP RITUAL &a&lCOMPLETED &a✓");
            MessageUtils.broadcastToWorld(world, "&7🌟 &fRitual Type: &d" + getRitualName(ritual.ritualType));
            MessageUtils.broadcastToWorld(world, "&7⏱ &fDuration: &e" + duration + "s");
            MessageUtils.broadcastToWorld(world, "&7💫 &fCollective dream energy &adissipating&f...");
            MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            MessageUtils.broadcastToWorld(world, "");
            
            // Completion effects
            Location center = calculateRitualCenter(ritual.participants);
            world.spawnParticle(Particle.TOTEM, center, 10, 2.0, 2.0, 2.0, 0.2);
            world.playSound(center, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.5f);
        }
    }
    
    /**
     * Get ritual name for display
     */
    private String getRitualName(RitualType ritualType) {
        switch (ritualType) {
            case HARMONY_CIRCLE: return "Harmony Circle Ritual";
            case DREAM_CONVERGENCE: return "Dream Convergence Ceremony";
            case ASTRAL_PROJECTION: return "Astral Projection Rite";
            case TEMPORAL_SYNC: return "Temporal Synchronization";
            case SOUL_BINDING: return "Soul Binding Ritual";
            case COSMIC_ALIGNMENT: return "Cosmic Alignment Ceremony";
            default: return "Unknown Ritual";
        }
    }
    
    /**
     * Check if world has active ritual
     */
    public boolean hasActiveRitual(World world) {
        return activeRituals.containsKey(world.getName());
    }
    
    /**
     * Get active ritual count
     */
    public int getActiveRitualCount() {
        return activeRituals.size();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        activeRituals.clear();
    }
    
    /**
     * Sleep ritual data class
     */
    private static class SleepRitual {
        final RitualType ritualType;
        final List<Player> participants;
        final long startTime;
        
        SleepRitual(RitualType ritualType, List<Player> participants, long startTime) {
            this.ritualType = ritualType;
            this.participants = new ArrayList<>(participants);
            this.startTime = startTime;
        }
    }
}