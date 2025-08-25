package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * 🌙 EXCLUSIVE DREAM SEQUENCE MANAGER 🌙
 * Revolutionary dream world system with stunning visual effects
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class DreamSequenceManager {
    
    private final EasySleep plugin;
    private final Map<UUID, DreamState> activeDreams;
    private final Map<UUID, BukkitTask> dreamTasks;
    private final Random random;
    
    // Dream types with unique effects
    private enum DreamType {
        PEACEFUL_MEADOW, COSMIC_VOYAGE, UNDERWATER_PALACE, 
        FLOATING_ISLANDS, CRYSTAL_CAVERNS, AURORA_REALM
    }
    
    public DreamSequenceManager(EasySleep plugin) {
        this.plugin = plugin;
        this.activeDreams = new HashMap<>();
        this.dreamTasks = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * 🌟 Start exclusive dream sequence for sleeping player
     */
    public void startDreamSequence(Player player) {
        if (!plugin.getConfigManager().areAnimationsEnabled()) {
            return;
        }
        
        UUID uuid = player.getUniqueId();
        DreamType dreamType = DreamType.values()[random.nextInt(DreamType.values().length)];
        DreamState dreamState = new DreamState(dreamType, System.currentTimeMillis());
        
        activeDreams.put(uuid, dreamState);
        
        // Send exclusive dream entry message
        MessageUtils.sendMessage(player, "");
        MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.sendMessage(player, "&d✨ &f&lDREAM REALM &d&lACCESS GRANTED &d✨");
        MessageUtils.sendMessage(player, "&7🌙 &fEntering: &b" + getDreamName(dreamType));
        MessageUtils.sendMessage(player, "&7⚡ &fDream ID: &e#" + Math.abs(uuid.hashCode() % 10000));
        MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.sendMessage(player, "");
        
        // Start dream sequence
        BukkitTask task = new BukkitRunnable() {
            int phase = 0;
            int ticks = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.isSleeping()) {
                    endDreamSequence(player);
                    cancel();
                    return;
                }
                
                switch (phase) {
                    case 0: // Dream entry (0-3 seconds)
                        createDreamEntry(player, dreamType, ticks);
                        if (ticks >= 60) { phase++; ticks = 0; }
                        break;
                    case 1: // Main dream experience (3-8 seconds)
                        createMainDreamExperience(player, dreamType, ticks);
                        if (ticks >= 100) { phase++; ticks = 0; }
                        break;
                    case 2: // Dream deepening (8-12 seconds)
                        createDreamDeepening(player, dreamType, ticks);
                        if (ticks >= 80) { phase++; ticks = 0; }
                        break;
                    case 3: // Continuous dream state
                        createContinuousDream(player, dreamType, ticks);
                        break;
                }
                
                ticks++;
            }
        }.runTaskTimer(plugin, 20L, 1L);
        
        dreamTasks.put(uuid, task);
    }
    
    /**
     * 🌟 Create dream entry effects
     */
    private void createDreamEntry(Player player, DreamType dreamType, int ticks) {
        Location loc = player.getLocation().add(0, 1.5, 0);
        
        // Spiral portal effect
        for (int i = 0; i < 3; i++) {
            double angle = (ticks * 0.3) + (i * 120);
            double radius = 1.5 - (ticks * 0.02);
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.1) * 0.5;
            
            Location portalLoc = loc.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.PORTAL, portalLoc, 2, 0.1, 0.1, 0.1, 0.02);
        }
        
        // Dream-specific entry particles
        switch (dreamType) {
            case PEACEFUL_MEADOW:
                player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0.5, 0.5, 0.5, 0.01);
                break;
            case COSMIC_VOYAGE:
                player.getWorld().spawnParticle(Particle.END_ROD, loc, 2, 0.3, 0.3, 0.3, 0.05);
                break;
            case UNDERWATER_PALACE:
                player.getWorld().spawnParticle(Particle.DRIP_WATER, loc, 3, 0.4, 0.4, 0.4, 0.01);
                break;
            case FLOATING_ISLANDS:
                player.getWorld().spawnParticle(Particle.CLOUD, loc, 2, 0.6, 0.2, 0.6, 0.02);
                break;
            case CRYSTAL_CAVERNS:
                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 3, 0.5, 0.5, 0.5, 0.03);
                break;
            case AURORA_REALM:
                player.getWorld().spawnParticle(Particle.TOTEM, loc, 1, 0.8, 0.8, 0.8, 0.02);
                break;
        }
        
        // Entry sound
        if (ticks == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 0.3f, 1.8f);
        }
    }
    
    /**
     * 🌟 Create main dream experience
     */
    private void createMainDreamExperience(Player player, DreamType dreamType, int ticks) {
        Location loc = player.getLocation().add(0, 2, 0);
        
        switch (dreamType) {
            case PEACEFUL_MEADOW:
                createPeacefulMeadowDream(player, loc, ticks);
                break;
            case COSMIC_VOYAGE:
                createCosmicVoyageDream(player, loc, ticks);
                break;
            case UNDERWATER_PALACE:
                createUnderwaterPalaceDream(player, loc, ticks);
                break;
            case FLOATING_ISLANDS:
                createFloatingIslandsDream(player, loc, ticks);
                break;
            case CRYSTAL_CAVERNS:
                createCrystalCavernsDream(player, loc, ticks);
                break;
            case AURORA_REALM:
                createAuroraRealmDream(player, loc, ticks);
                break;
        }
        
        // Dream progression message
        if (ticks == 50) {
            MessageUtils.sendMessage(player, "&7💫 &fDream sequence &bdeepening&f... &7💫");
        }
    }
    
    /**
     * 🌸 Peaceful Meadow Dream
     */
    private void createPeacefulMeadowDream(Player player, Location center, int ticks) {
        // Floating flowers
        for (int i = 0; i < 2; i++) {
            double angle = ticks * 0.1 + (i * 180);
            double radius = 1.2 + Math.sin(ticks * 0.05) * 0.3;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.08 + i) * 0.4;
            
            Location flowerLoc = center.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, flowerLoc, 1, 0.1, 0.1, 0.1, 0.01);
        }
        
        // Gentle breeze effect
        if (ticks % 30 == 0) {
            player.getWorld().spawnParticle(Particle.CLOUD, center, 3, 1.5, 0.5, 1.5, 0.02);
            player.playSound(player.getLocation(), Sound.BLOCK_GRASS_STEP, 0.1f, 2.0f);
        }
    }
    
    /**
     * 🌌 Cosmic Voyage Dream
     */
    private void createCosmicVoyageDream(Player player, Location center, int ticks) {
        // Orbiting stars
        for (int i = 0; i < 4; i++) {
            double angle = ticks * 0.2 + (i * 90);
            double radius = 2.0 + Math.sin(ticks * 0.03 + i) * 0.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.cos(ticks * 0.04 + i) * 0.8;
            
            Location starLoc = center.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.END_ROD, starLoc, 1, 0, 0, 0, 0);
        }
        
        // Cosmic wind
        if (ticks % 25 == 0) {
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, center, 2, 1.0, 1.0, 1.0, 0.05);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 0.08f, 2.5f);
        }
    }
    
    /**
     * 🌊 Underwater Palace Dream
     */
    private void createUnderwaterPalaceDream(Player player, Location center, int ticks) {
        // Bubble streams
        for (int i = 0; i < 3; i++) {
            double angle = i * 120;
            double x = Math.cos(Math.toRadians(angle)) * 0.8;
            double z = Math.sin(Math.toRadians(angle)) * 0.8;
            double y = (ticks % 40) * 0.1;
            
            Location bubbleLoc = center.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.BUBBLE_POP, bubbleLoc, 1, 0.1, 0.1, 0.1, 0.01);
        }
        
        // Water currents
        if (ticks % 20 == 0) {
            player.getWorld().spawnParticle(Particle.DRIP_WATER, center, 5, 1.2, 0.8, 1.2, 0.02);
            player.playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_LOOP, 0.12f, 1.5f);
        }
    }
    
    /**
     * ☁️ Floating Islands Dream
     */
    private void createFloatingIslandsDream(Player player, Location center, int ticks) {
        // Floating cloud platforms
        for (int i = 0; i < 2; i++) {
            double angle = ticks * 0.08 + (i * 180);
            double radius = 1.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.06 + i) * 0.6 + 1.0;
            
            Location cloudLoc = center.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.CLOUD, cloudLoc, 2, 0.3, 0.1, 0.3, 0.01);
        }
        
        // Wind effects
        if (ticks % 35 == 0) {
            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, center, 1, 1.5, 1.5, 1.5, 0.1);
            player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 0.1f, 1.8f);
        }
    }
    
    /**
     * 💎 Crystal Caverns Dream
     */
    private void createCrystalCavernsDream(Player player, Location center, int ticks) {
        // Crystalline formations
        for (int i = 0; i < 6; i++) {
            double angle = i * 60;
            double radius = 1.0 + Math.sin(ticks * 0.04 + i) * 0.2;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.cos(ticks * 0.05 + i) * 0.3;
            
            Location crystalLoc = center.clone().add(x, y, z);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, crystalLoc, 1, 0.05, 0.05, 0.05, 0.02);
        }
        
        // Crystal resonance
        if (ticks % 40 == 0) {
            player.getWorld().spawnParticle(Particle.CRIT_MAGIC, center, 4, 1.0, 1.0, 1.0, 0.1);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 0.15f, 2.2f);
        }
    }
    
    /**
     * 🌈 Aurora Realm Dream
     */
    private void createAuroraRealmDream(Player player, Location center, int ticks) {
        // Aurora waves
        for (int i = 0; i < 8; i++) {
            double angle = ticks * 0.15 + (i * 45);
            double radius = 2.5 + Math.sin(ticks * 0.02 + i) * 0.8;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(ticks * 0.07 + i) * 1.2;
            
            Location auroraLoc = center.clone().add(x, y, z);
            
            // Cycle through aurora colors
            Particle auroraParticle = (i % 3 == 0) ? Particle.TOTEM : 
                                    (i % 3 == 1) ? Particle.SOUL_FIRE_FLAME : Particle.END_ROD;
            player.getWorld().spawnParticle(auroraParticle, auroraLoc, 1, 0.1, 0.1, 0.1, 0.01);
        }
        
        // Aurora sounds
        if (ticks % 45 == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 0.1f, 2.0f);
        }
    }
    
    /**
     * 🌟 Create dream deepening effects
     */
    private void createDreamDeepening(Player player, DreamType dreamType, int ticks) {
        Location loc = player.getLocation().add(0, 1.8, 0);
        
        // Deepening spiral
        double angle = ticks * 0.4;
        double radius = 0.8 + Math.sin(ticks * 0.1) * 0.3;
        double x = Math.cos(Math.toRadians(angle)) * radius;
        double z = Math.sin(Math.toRadians(angle)) * radius;
        double y = Math.sin(ticks * 0.12) * 0.4;
        
        Location spiralLoc = loc.clone().add(x, y, z);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, spiralLoc, 1, 0.05, 0.05, 0.05, 0.01);
        
        // Deepening message
        if (ticks == 40) {
            MessageUtils.sendMessage(player, "&7🌙 &fDream state &dstabilized&f... entering &bdeep sleep&f... &7🌙");
        }
    }
    
    /**
     * 🌟 Create continuous dream state
     */
    private void createContinuousDream(Player player, DreamType dreamType, int ticks) {
        // Gentle continuous effects every 3 seconds
        if (ticks % 60 == 0) {
            Location loc = player.getLocation().add(0, 1.5, 0);
            
            // Gentle dream maintenance particles
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0.2, 0.2, 0.2, 0.005);
            
            // Soft dream sound
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.05f, 2.5f);
        }
    }
    
    /**
     * 🌟 End dream sequence
     */
    public void endDreamSequence(Player player) {
        UUID uuid = player.getUniqueId();
        DreamState dreamState = activeDreams.remove(uuid);
        BukkitTask task = dreamTasks.remove(uuid);
        
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
        
        if (dreamState != null) {
            long duration = (System.currentTimeMillis() - dreamState.startTime) / 1000;
            
            // Dream exit message
            MessageUtils.sendMessage(player, "");
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "&c⚠ &f&lDREAM SEQUENCE &c&lTERMINATED &c⚠");
            MessageUtils.sendMessage(player, "&7🌅 &fReturning to &areality&f...");
            MessageUtils.sendMessage(player, "&7⏱ &fDream Duration: &e" + duration + "s");
            MessageUtils.sendMessage(player, "&7💫 &fDream Type: &b" + getDreamName(dreamState.dreamType));
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "");
            
            // Exit effects
            Location loc = player.getLocation().add(0, 1.5, 0);
            player.getWorld().spawnParticle(Particle.TOTEM, loc, 5, 0.5, 0.5, 0.5, 0.1);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2f, 2.0f);
        }
    }
    
    /**
     * Get dream name for display
     */
    private String getDreamName(DreamType dreamType) {
        switch (dreamType) {
            case PEACEFUL_MEADOW: return "Peaceful Meadow Sanctuary";
            case COSMIC_VOYAGE: return "Cosmic Voyage Dimension";
            case UNDERWATER_PALACE: return "Underwater Crystal Palace";
            case FLOATING_ISLANDS: return "Floating Sky Islands";
            case CRYSTAL_CAVERNS: return "Enchanted Crystal Caverns";
            case AURORA_REALM: return "Aurora Borealis Realm";
            default: return "Unknown Dream Realm";
        }
    }
    
    /**
     * Check if player is in dream state
     */
    public boolean isInDreamState(Player player) {
        return activeDreams.containsKey(player.getUniqueId());
    }
    
    /**
     * Get active dream count
     */
    public int getActiveDreamCount() {
        return activeDreams.size();
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        for (BukkitTask task : dreamTasks.values()) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        dreamTasks.clear();
        activeDreams.clear();
    }
    
    /**
     * Dream state data class
     */
    private static class DreamState {
        final DreamType dreamType;
        final long startTime;
        
        DreamState(DreamType dreamType, long startTime) {
            this.dreamType = dreamType;
            this.startTime = startTime;
        }
    }
}