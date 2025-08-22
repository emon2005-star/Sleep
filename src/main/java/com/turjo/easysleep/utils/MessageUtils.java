package com.turjo.easysleep.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Utility class for handling chat messages and formatting
 * 
 * Provides methods for sending formatted messages to players
 * with color code support.
 * 
 * @author Turjo
 */
public class MessageUtils {
    
    /**
     * Send a formatted message to a command sender
     * 
     * @param sender The command sender to send the message to
     * @param message The message with color codes (&-format)
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(message));
    }
    
    /**
     * Convert color codes from & format to ChatColor format
     * 
     * @param message The message with & color codes
     * @return The message with ChatColor formatting
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Send multiple formatted messages to a command sender
     * 
     * @param sender The command sender to send the messages to
     * @param messages Array of messages with color codes
     */
    public static void sendMessages(CommandSender sender, String... messages) {
        for (String message : messages) {
            sendMessage(sender, message);
        }
    }
    
    /**
     * Create a formatted prefix for plugin messages
     * 
     * @return Formatted plugin prefix
     */
    public static String getPrefix() {
        return colorize("&8[&6EasySleep&8] &r");
    }
    
    /**
     * Send a message with the plugin prefix
     * 
     * @param sender The command sender to send the message to
     * @param message The message content
     */
    public static void sendPrefixedMessage(CommandSender sender, String message) {
        sendMessage(sender, getPrefix() + message);
    }
    
    /**
     * Broadcast a message to all players in a specific world
     * 
     * @param world The world to broadcast to
     * @param message The message with color codes
     */
    public static void broadcastToWorld(org.bukkit.World world, String message) {
        for (org.bukkit.entity.Player player : world.getPlayers()) {
            sendMessage(player, message);
        }
    }
}