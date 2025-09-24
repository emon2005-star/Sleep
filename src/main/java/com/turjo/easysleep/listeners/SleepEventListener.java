package com.turjo.easysleep.listeners;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.managers.AnimationManager;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.GameRule;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Event listener for sleep-related events
 * 
 * Handles player bed enter/leave events, time skip events, and world loading
 * with rewards, effects, and enhanced sleep management.
 * 
 * @author Turjo
 * @version 1.5.1
 */
public class SleepEventListener implements Listener {
    
    private final EasySleep plugin;
    private final AnimationManager animationManager;
    
    public SleepEventListener(EasySleep plugin) {
        this.plugin = plugin;
        this.animationManager = plugin.getAnimationManager();
    }
    
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }
        
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        // Record sleep event
        plugin.getStatisticsManager().recordSleepEvent();
        
        // Give sleep rewards (this includes dream coins, so we don't duplicate)
        if (player.hasPermission("easysleep.rewards")) {
            plugin.getRewardsManager().giveSleepRewards(player);
        }
        
        // Check achievements
        if (player.hasPermission("easysleep.achievements")) {
            plugin.getSleepAchievementManager().checkSleepAchievements(player);
            plugin.getSleepAchievementManager().checkDimensionalAchievements(player);
        }
        
        // Check moon phase achievements
        com.turjo.easysleep.managers.MoonPhaseManager.MoonPhase moonPhase = plugin.getMoonPhaseManager().getCurrentMoonPhase(world);
        if (moonPhase != null && player.hasPermission("easysleep.achievements")) {
            plugin.getSleepAchievementManager().checkMoonPhaseAchievements(player, moonPhase.name());
        }
        
        // Check anti-spam
        if (!plugin.getAntiSpamManager().canSendSleepMessage(player)) {
            return;
        }
        
        // Start time acceleration if enabled
        startTimeAcceleration(world);
        
        // Start sleep animation if animations are enabled
        if (plugin.getConfigManager().areAnimationsEnabled()) {
            // Delay animation start slightly to ensure player is properly in bed
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline() && player.isSleeping()) {
                        animationManager.startSleepAnimation(player);
                    }
                }
            }.runTaskLater(plugin, 5L);
        }
        
        // Get sleep statistics
        int totalPlayers = world.getPlayers().size();
        int sleepingPlayers = getSleepingPlayerCount(world);
        int activePlayers = getActivePlayerCount(world);
        
        // Calculate required players based on percentage
        Integer sleepPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int requiredPlayers = (int) Math.ceil((sleepPercentage != null ? sleepPercentage : 50) * activePlayers / 100.0);
        
        // Broadcast sleep status if enabled
        if (plugin.getConfigManager().isMessageCategoryEnabled("sleep-messages")) {
            String sleepMessage = plugin.getConfigManager().getMessage("sleep.player-sleeping",
                "%player%", player.getName(),
                "%sleeping%", String.valueOf(sleepingPlayers),
                "%required%", String.valueOf(requiredPlayers));
            
            for (Player p : world.getPlayers()) {
                if (!p.equals(player)) {
                    MessageUtils.sendMessage(p, sleepMessage);
                }
            }
        }
    }
    
    /**
     * Start time acceleration when any player sleeps
     */
    private void startTimeAcceleration(World world) {
        double acceleration = plugin.getConfigManager().getConfig().getDouble("sleep.time-acceleration", 1.75);
        
        if (acceleration > 1.0) {
            // Start time acceleration task
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getSleepingPlayerCount(world) == 0) {
                        cancel();
                        return;
                    }
                    
                    // Accelerate time
                    long currentTime = world.getTime();
                    long newTime = currentTime + (long) (20 * (acceleration - 1.0));
                    world.setTime(newTime);
                }
            }.runTaskTimer(plugin, 0L, 1L);
            
            // Broadcast acceleration message
            if (plugin.getConfigManager().isMessageCategoryEnabled("time-acceleration")) {
                String accelMessage = plugin.getConfigManager().getMessage("time-acceleration.flowing-faster",
                    "%speed%", String.format("%.1f", acceleration));
                MessageUtils.broadcastToWorld(world, accelMessage);
            }
        }
    }
    
    /**
     * Get count of sleeping players (excluding AFK)
     */
    private int getSleepingPlayerCount(World world) {
        int count = 0;
        for (Player player : world.getPlayers()) {
            if (player.isSleeping() && !plugin.getAFKManager().isPlayerAFK(player)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get count of active players (excluding AFK)
     */
    private int getActivePlayerCount(World world) {
        int count = 0;
        for (Player player : world.getPlayers()) {
            if (!plugin.getAFKManager().isPlayerAFK(player)) {
                count++;
            }
        }
        return count;
    }
    
    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        
        // Check anti-spam
        if (!plugin.getAntiSpamManager().canSendWakeMessage(player)) {
            return;
        }
        
        // Stop sleep animation
        animationManager.stopAnimation(player);
        
        // Get updated sleep statistics
        int totalPlayers = world.getPlayers().size();
        int sleepingPlayers = getSleepingPlayerCount(world);
        
        // Broadcast wake up message if enabled
        if (plugin.getConfigManager().isMessageCategoryEnabled("sleep-messages")) {
            String wakeMessage = plugin.getConfigManager().getMessage("sleep.player-waking",
                "%player%", player.getName(),
                "%sleeping%", String.valueOf(sleepingPlayers),
                "%total%", String.valueOf(totalPlayers));
            
            MessageUtils.broadcastToWorld(world, wakeMessage);
        }
    }
    
    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            World world = event.getWorld();
            
            // Record night skip
            plugin.getStatisticsManager().recordNightSkip();
            
            // Give morning effects to all players
            giveMorningEffects(world);
            
            // Start night skip animation if enabled
            if (plugin.getConfigManager().areAnimationsEnabled()) {
                plugin.getAnimationManager().startNightSkipAnimation(world);
            }
            
            // Broadcast night skip message
            double acceleration = plugin.getConfigManager().getConfig().getDouble("sleep.time-acceleration", 1.75);
            if (plugin.getConfigManager().isMessageCategoryEnabled("night-skip-messages")) {
                String skipMessage = plugin.getConfigManager().getMessage("night-skip.announcement",
                    "%speed%", String.format("%.1f", acceleration));
                MessageUtils.broadcastToWorld(world, skipMessage);
            }
        }
    }
    
    /**
     * Give morning effects to all players in world
     */
    private void giveMorningEffects(World world) {
        if (!plugin.getConfigManager().getConfig().getBoolean("rewards.effects.enabled", true)) {
            return;
        }
        
        List<String> morningEffects = plugin.getConfigManager().getConfig().getStringList("rewards.effects.morning-effects");
        
        for (Player player : world.getPlayers()) {
            for (String effectString : morningEffects) {
                org.bukkit.potion.PotionEffect effect = parseEffectString(effectString);
                if (effect != null) {
                    player.addPotionEffect(effect);
                }
            }
        }
    }
    
    /**
     * Parse effect string format: "EFFECT_NAME:DURATION:AMPLIFIER"
     */
    private org.bukkit.potion.PotionEffect parseEffectString(String effectString) {
        try {
            String[] parts = effectString.split(":");
            org.bukkit.potion.PotionEffectType effectType = org.bukkit.potion.PotionEffectType.getByName(parts[0].toUpperCase());
            int duration = parts.length > 1 ? Integer.parseInt(parts[1]) * 20 : 600; // Convert to ticks
            int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            
            if (effectType != null) {
                return new org.bukkit.potion.PotionEffect(effectType, duration, amplifier);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid effect format: " + effectString);
        }
        return null;
    }
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        
        // Auto-configure new worlds with default sleep percentage
        if (plugin.getConfigManager().getConfig().getBoolean("sleep.auto-configure-new-worlds", true)) {
            Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
            int defaultPercentage = plugin.getConfigManager().getConfig().getInt("sleep.default-percentage", 50);
            
            if (currentPercentage == null || currentPercentage != defaultPercentage) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, defaultPercentage);
                plugin.getLogger().info("Auto-configured world '" + world.getName() + "' with " + defaultPercentage + "% sleep requirement");
            }
        }
    }
}