package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Automatic update checker for EasySleep plugin
 * Checks SpigotMC resource page for new versions
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class UpdateChecker {
    
    private final EasySleep plugin;
    private final int resourceId = 127995; // SpigotMC resource ID
    private String latestVersion;
    private boolean updateAvailable = false;
    
    public UpdateChecker(EasySleep plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Check for updates asynchronously
     */
    public void checkForUpdates() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    getVersion(version -> {
                        latestVersion = version;
                        String currentVersion = plugin.getDescription().getVersion();
                        
                        if (!currentVersion.equals(version)) {
                            updateAvailable = true;
                            
                            // Log to console
                            plugin.getLogger().info("╔═══════════════════════════════════════════╗");
                            plugin.getLogger().info("║        🔄 UPDATE AVAILABLE! 🔄            ║");
                            plugin.getLogger().info("║                                           ║");
                            plugin.getLogger().info("║ Current Version: " + String.format("%-20s", currentVersion) + "║");
                            plugin.getLogger().info("║ Latest Version:  " + String.format("%-20s", version) + "║");
                            plugin.getLogger().info("║                                           ║");
                            plugin.getLogger().info("║ Download: spigotmc.org/resources/127995   ║");
                            plugin.getLogger().info("╚═══════════════════════════════════════════╝");
                            
                            // Notify online admins
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.hasPermission("easysleep.admin")) {
                                        player.sendMessage("§6╔═══════════════════════════════════════════╗");
                                        player.sendMessage("§6║ §b🔄 §f§lEASYSLEEP UPDATE AVAILABLE §b🔄 §6║");
                                        player.sendMessage("§6║ §fCurrent: §e" + currentVersion + " §7→ §fLatest: §a" + version + " §6║");
                                        player.sendMessage("§6║ §7Download: §espigotmc.org/resources/127995 §6║");
                                        player.sendMessage("§6╚═══════════════════════════════════════════╝");
                                    }
                                }
                            });
                        } else {
                            plugin.getLogger().info("✓ EasySleep is up to date! (v" + currentVersion + ")");
                        }
                    });
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not check for updates: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    
    /**
     * Get version from SpigotMC API
     */
    private void getVersion(Consumer<String> consumer) {
        try {
            InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
                    .openStream();
            Scanner scanner = new Scanner(inputStream);
            
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            plugin.getLogger().warning("Cannot look for updates: " + e.getMessage());
        }
    }
    
    /**
     * Check if update is available
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
    
    /**
     * Get latest version string
     */
    public String getLatestVersion() {
        return latestVersion;
    }
    
    /**
     * Start periodic update checking
     */
    public void startPeriodicCheck() {
        // Check every 6 hours
        new BukkitRunnable() {
            @Override
            public void run() {
                checkForUpdates();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 432000L); // 6 hours = 432000 ticks
    }
}