package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * 🌙 EXCLUSIVE MOON PHASE MANAGER 🌙
 * Revolutionary lunar cycle system with unique sleep bonuses
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class MoonPhaseManager {
    
    private final EasySleep plugin;
    private final Map<String, MoonPhase> worldMoonPhases;
    private final Map<String, Long> lastPhaseCheck;
    
    // Moon phases with unique effects
    public enum MoonPhase {
        NEW_MOON("New Moon", "🌑", 0.8, "Shadow dreams and deep rest"),
        WAXING_CRESCENT("Waxing Crescent", "🌒", 1.0, "Growing energy and hope"),
        FIRST_QUARTER("First Quarter", "🌓", 1.2, "Balance and harmony"),
        WAXING_GIBBOUS("Waxing Gibbous", "🌔", 1.4, "Increasing power and clarity"),
        FULL_MOON("Full Moon", "🌕", 2.0, "Maximum lunar energy and magic"),
        WANING_GIBBOUS("Waning Gibbous", "🌖", 1.4, "Wisdom and reflection"),
        LAST_QUARTER("Last Quarter", "🌗", 1.2, "Release and letting go"),
        WANING_CRESCENT("Waning Crescent", "🌘", 1.0, "Preparation and renewal");
        
        private final String name;
        private final String symbol;
        private final double sleepBonus;
        private final String description;
        
        MoonPhase(String name, String symbol, double sleepBonus, String description) {
            this.name = name;
            this.symbol = symbol;
            this.sleepBonus = sleepBonus;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public double getSleepBonus() { return sleepBonus; }
        public String getDescription() { return description; }
    }
    
    public MoonPhaseManager(EasySleep plugin) {
        this.plugin = plugin;
        this.worldMoonPhases = new HashMap<>();
        this.lastPhaseCheck = new HashMap<>();
        startMoonPhaseTracking();
    }
    
    /**
     * 🌟 Start moon phase tracking system
     */
    private void startMoonPhaseTracking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : plugin.getServer().getWorlds()) {
                    updateMoonPhase(world);
                }
            }
        }.runTaskTimer(plugin, 0L, 1200L); // Check every minute
    }
    
    /**
     * 🌟 Update moon phase for world
     */
    private void updateMoonPhase(World world) {
        String worldName = world.getName();
        long currentDay = world.getFullTime() / 24000L;
        long lastCheck = lastPhaseCheck.getOrDefault(worldName, -1L);
        
        if (currentDay != lastCheck) {
            MoonPhase oldPhase = worldMoonPhases.get(worldName);
            MoonPhase newPhase = calculateMoonPhase(currentDay);
            
            worldMoonPhases.put(worldName, newPhase);
            lastPhaseCheck.put(worldName, currentDay);
            
            // Announce phase change if different
            if (oldPhase != newPhase) {
                announceMoonPhaseChange(world, newPhase);
                createMoonPhaseEffects(world, newPhase);
            }
        }
    }
    
    /**
     * 🌟 Calculate moon phase based on day
     */
    private MoonPhase calculateMoonPhase(long day) {
        int cycle = (int) (day % 8); // 8-day lunar cycle
        return MoonPhase.values()[cycle];
    }
    
    /**
     * 🌟 Announce moon phase change
     */
    private void announceMoonPhaseChange(World world, MoonPhase moonPhase) {
        // Only announce during night time
        if (world.getTime() < 12000) {
            return;
        }
        
        MessageUtils.broadcastToWorld(world, "");
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "&f🌙 &f&lLUNAR PHASE TRANSITION &f🌙");
        MessageUtils.broadcastToWorld(world, "&7✨ &fCurrent Phase: " + moonPhase.getSymbol() + " &e" + moonPhase.getName());
        MessageUtils.broadcastToWorld(world, "&7🌟 &fSleep Bonus: &a+" + (int)((moonPhase.getSleepBonus() - 1.0) * 100) + "%");
        MessageUtils.broadcastToWorld(world, "&7💫 &fLunar Energy: &b" + moonPhase.getDescription());
        MessageUtils.broadcastToWorld(world, "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        MessageUtils.broadcastToWorld(world, "");
    }
    
    /**
     * 🌟 Create moon phase effects
     */
    private void createMoonPhaseEffects(World world, MoonPhase moonPhase) {
        // Find a central location for effects
        Location center = world.getSpawnLocation().add(0, 50, 0);
        
        // Phase-specific visual effects
        switch (moonPhase) {
            case NEW_MOON:
                createNewMoonEffects(world, center);
                break;
            case WAXING_CRESCENT:
                createWaxingCrescentEffects(world, center);
                break;
            case FIRST_QUARTER:
                createFirstQuarterEffects(world, center);
                break;
            case WAXING_GIBBOUS:
                createWaxingGibbousEffects(world, center);
                break;
            case FULL_MOON:
                createFullMoonEffects(world, center);
                break;
            case WANING_GIBBOUS:
                createWaningGibbousEffects(world, center);
                break;
            case LAST_QUARTER:
                createLastQuarterEffects(world, center);
                break;
            case WANING_CRESCENT:
                createWaningCrescentEffects(world, center);
                break;
        }
        
        // Phase transition sound
        world.playSound(center, Sound.BLOCK_BEACON_POWER_SELECT, 0.3f, getMoonPhasePitch(moonPhase));
    }
    
    /**
     * 🌑 New Moon Effects
     */
    private void createNewMoonEffects(World world, Location center) {
        // Dark, mysterious particles
        for (int i = 0; i < 20; i++) {
            double angle = i * 18;
            double radius = 8.0;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.SMOKE_LARGE, effectLoc, 2, 0.5, 0.5, 0.5, 0.02);
        }
    }
    
    /**
     * 🌒 Waxing Crescent Effects
     */
    private void createWaxingCrescentEffects(World world, Location center) {
        // Growing light particles
        for (int i = 0; i < 15; i++) {
            double angle = i * 24;
            double radius = 6.0;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.END_ROD, effectLoc, 1, 0.2, 0.2, 0.2, 0.01);
        }
    }
    
    /**
     * 🌓 First Quarter Effects
     */
    private void createFirstQuarterEffects(World world, Location center) {
        // Balanced light and shadow
        for (int i = 0; i < 16; i++) {
            double angle = i * 22.5;
            double radius = 7.0;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            Particle particle = (i % 2 == 0) ? Particle.SOUL_FIRE_FLAME : Particle.SMOKE_NORMAL;
            world.spawnParticle(particle, effectLoc, 1, 0.3, 0.3, 0.3, 0.02);
        }
    }
    
    /**
     * 🌔 Waxing Gibbous Effects
     */
    private void createWaxingGibbousEffects(World world, Location center) {
        // Increasing brightness
        for (int i = 0; i < 25; i++) {
            double angle = i * 14.4;
            double radius = 9.0;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, effectLoc, 2, 0.4, 0.4, 0.4, 0.03);
        }
    }
    
    /**
     * 🌕 Full Moon Effects
     */
    private void createFullMoonEffects(World world, Location center) {
        // Maximum lunar energy - spectacular display
        for (int i = 0; i < 40; i++) {
            double angle = i * 9;
            double radius = 12.0;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            double y = Math.sin(Math.toRadians(angle * 2)) * 2.0;
            
            Location effectLoc = center.clone().add(x, y, z);
            world.spawnParticle(Particle.TOTEM, effectLoc, 3, 0.5, 0.5, 0.5, 0.05);
        }
        
        // Central energy burst
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, center, 20, 3.0, 3.0, 3.0, 0.1);
        
        // Lightning effect for dramatic impact
        world.strikeLightningEffect(center);
    }
    
    /**
     * 🌖 Waning Gibbous Effects
     */
    private void createWaningGibbousEffects(World world, Location center) {
        // Wise, reflective particles
        for (int i = 0; i < 22; i++) {
            double angle = i * 16.4;
            double radius = 8.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.SOUL, effectLoc, 1, 0.3, 0.3, 0.3, 0.02);
        }
    }
    
    /**
     * 🌗 Last Quarter Effects
     */
    private void createLastQuarterEffects(World world, Location center) {
        // Release and letting go
        for (int i = 0; i < 18; i++) {
            double angle = i * 20;
            double radius = 7.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.CLOUD, effectLoc, 2, 0.4, 0.4, 0.4, 0.03);
        }
    }
    
    /**
     * 🌘 Waning Crescent Effects
     */
    private void createWaningCrescentEffects(World world, Location center) {
        // Preparation for renewal
        for (int i = 0; i < 12; i++) {
            double angle = i * 30;
            double radius = 5.5;
            double x = Math.cos(Math.toRadians(angle)) * radius;
            double z = Math.sin(Math.toRadians(angle)) * radius;
            
            Location effectLoc = center.clone().add(x, 0, z);
            world.spawnParticle(Particle.PORTAL, effectLoc, 1, 0.2, 0.2, 0.2, 0.01);
        }
    }
    
    /**
     * 🌟 Get moon phase pitch for sounds
     */
    private float getMoonPhasePitch(MoonPhase moonPhase) {
        switch (moonPhase) {
            case NEW_MOON: return 0.8f;
            case WAXING_CRESCENT: return 1.0f;
            case FIRST_QUARTER: return 1.2f;
            case WAXING_GIBBOUS: return 1.4f;
            case FULL_MOON: return 2.0f;
            case WANING_GIBBOUS: return 1.4f;
            case LAST_QUARTER: return 1.2f;
            case WANING_CRESCENT: return 1.0f;
            default: return 1.0f;
        }
    }
    
    /**
     * 🌟 Apply moon phase sleep bonus
     */
    public void applyMoonPhaseBonus(Player player) {
        World world = player.getWorld();
        MoonPhase moonPhase = getCurrentMoonPhase(world);
        
        if (moonPhase != null && moonPhase.getSleepBonus() > 1.0) {
            // Send bonus message
            MessageUtils.sendMessage(player, "");
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "&f🌙 &f&lLUNAR BLESSING RECEIVED &f🌙");
            MessageUtils.sendMessage(player, "&7✨ &fMoon Phase: " + moonPhase.getSymbol() + " &e" + moonPhase.getName());
            MessageUtils.sendMessage(player, "&7🌟 &fSleep Bonus: &a+" + (int)((moonPhase.getSleepBonus() - 1.0) * 100) + "% &feffectiveness");
            MessageUtils.sendMessage(player, "&7💫 &b" + moonPhase.getDescription());
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "");
            
            // Lunar blessing effects
            Location loc = player.getLocation().add(0, 1.5, 0);
            player.getWorld().spawnParticle(Particle.TOTEM, loc, 5, 0.5, 0.5, 0.5, 0.1);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.3f, 1.8f);
        }
    }
    
    /**
     * Get current moon phase for world
     */
    public MoonPhase getCurrentMoonPhase(World world) {
        return worldMoonPhases.get(world.getName());
    }
    
    /**
     * Get moon phase info for status command
     */
    public String getMoonPhaseInfo(World world) {
        MoonPhase moonPhase = getCurrentMoonPhase(world);
        if (moonPhase != null) {
            return moonPhase.getSymbol() + " " + moonPhase.getName() + " (+" + 
                   (int)((moonPhase.getSleepBonus() - 1.0) * 100) + "%)";
        }
        return "🌙 Unknown Phase";
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        worldMoonPhases.clear();
        lastPhaseCheck.clear();
    }
}