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

/**
 * Event listener for sleep-related events
 * 
 * Handles player bed enter/leave events, time skip events, and world loading
 * with real-time sleep status updates and futuristic styling.
 * 
 * @author Turjo
 * @version 1.3.1
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
        
        // Start exclusive dream sequence
        plugin.getDreamSequenceManager().startDreamSequence(player);
        
        // Apply moon phase bonus
        plugin.getMoonPhaseManager().applyMoonPhaseBonus(player);
        
        // Check anti-spam
        if (!plugin.getAntiSpamManager().canSendSleepMessage(player)) {
            return;
        }
        
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
        long sleepingCount = world.getPlayers().stream()
                .filter(Player::isSleeping)
                .count();
        int sleepingPlayers = (int) sleepingCount;
        
        // Calculate required players based on percentage
        Integer sleepPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int requiredPlayers = (int) Math.ceil((sleepPercentage != null ? sleepPercentage : 100) * totalPlayers / 100.0);
        
        // Broadcast sleep status if enabled
        if (plugin.getConfigManager().shouldBroadcastSleepMessages()) {
            // Enhanced futuristic messages for sleeping and awake players
            MessageUtils.sendMessage(player, "");
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "&b💤 &f&lDREAM SEQUENCE &b&lINITIATED &b💤");
            MessageUtils.sendMessage(player, "&7┌─ &fYou drift into &bpeaceful dreams&f...");
            MessageUtils.sendMessage(player, "&7├─ &fSleep Protocol: &a" + sleepingPlayers + "&7/&e" + requiredPlayers + " &7dreamers");
            MessageUtils.sendMessage(player, "&7└─ &fStatus: &b⚡ DREAM ENERGY FLOWING");
            MessageUtils.sendMessage(player, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.sendMessage(player, "");
            
            for (Player p : world.getPlayers()) {
                if (!p.equals(player)) {
                    MessageUtils.sendMessage(p, "&7🌙 &e" + player.getName() + " &7enters &bdream state &8(&a" + sleepingPlayers + "&7/&e" + requiredPlayers + "&8)");
                }
            }
        }
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
        
        // End dream sequence
        plugin.getDreamSequenceManager().endDreamSequence(player);
        
        // Get updated sleep statistics
        int totalPlayers = world.getPlayers().size();
        long sleepingCount = world.getPlayers().stream()
                .filter(Player::isSleeping)
                .count();
        int sleepingPlayers = (int) sleepingCount;
        
        // Broadcast wake up message if enabled
        if (plugin.getConfigManager().shouldBroadcastSleepMessages()) {
            // Enhanced futuristic wake up message
            MessageUtils.broadcastToWorld(world, "");
            MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.broadcastToWorld(world, "&c⚠ &f&lDREAM PROTOCOL &c&lINTERRUPTED &c⚠");
            MessageUtils.broadcastToWorld(world, "&7┌─ &e" + player.getName() + " &7has &cabruptly left dream state");
            MessageUtils.broadcastToWorld(world, "&7├─ &fActive Dreamers: &a" + sleepingPlayers + "&7/&c" + totalPlayers + " &7souls");
            MessageUtils.broadcastToWorld(world, "&7├─ &fDream Energy: &c&lDISRUPTED");
            MessageUtils.broadcastToWorld(world, "&7└─ &fStatus: &c⚠ TIME SKIP PROTOCOL CANCELLED");
            MessageUtils.broadcastToWorld(world, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            MessageUtils.broadcastToWorld(world, "");
        }
    }
    
    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            World world = event.getWorld();
            
            // Record night skip
            plugin.getStatisticsManager().recordNightSkip();
            
            // Start night skip animation if enabled
            if (plugin.getConfigManager().areAnimationsEnabled()) {
                plugin.getAnimationManager().startNightSkipAnimation(world);
                
                // Check for sleep rituals
                plugin.getSleepRitualManager().checkSleepRitual(world);
            }
        }
    }
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        
        // Auto-configure new worlds with default sleep percentage
        if (plugin.getConfigManager().shouldAutoConfigureNewWorlds()) {
            Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
            int defaultPercentage = plugin.getConfigManager().getDefaultSleepPercentage();
            
            if (currentPercentage == null || currentPercentage == 100) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, defaultPercentage);
                plugin.getLogger().info("Auto-configured world '" + world.getName() + "' with " + defaultPercentage + "% sleep requirement");
            }
        }
    }
}