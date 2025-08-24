package com.turjo.easysleep.listeners;

import com.turjo.easysleep.EasySleep;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Event listener for player-related events
 * Handles AFK detection and player tracking
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class PlayerEventListener implements Listener {
    
    private final EasySleep plugin;
    
    public PlayerEventListener(EasySleep plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Update statistics
        plugin.getStatisticsManager().updatePlayerCount(plugin.getServer().getOnlinePlayers().size());
        
        // Check for updates (admin only)
        if (player.hasPermission("easysleep.admin")) {
            if (plugin.getUpdateChecker().isUpdateAvailable()) {
                player.sendMessage("§6╔═══════════════════════════════════════════╗");
                player.sendMessage("§6║ §b🔄 §f§lEASYSLEEP UPDATE AVAILABLE §b🔄 §6║");
                player.sendMessage("§6║ §fLatest: §a" + plugin.getUpdateChecker().getLatestVersion() + " §6║");
                player.sendMessage("§6║ §7Download: §espigotmc.org/resources/127995 §6║");
                player.sendMessage("§6╚═══════════════════════════════════════════╝");
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Update AFK status
        plugin.getAFKManager().updateActivity(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Clean up tracking data
        plugin.getAFKManager().removePlayer(player);
        plugin.getAntiSpamManager().removePlayer(player);
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        // Handle pending GUI input - run on main thread
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (plugin.getSleepGUI().hasPendingInput(player)) {
                event.setCancelled(true);
                plugin.getSleepGUI().handleChatInput(player, message);
            }
        });
    }
}