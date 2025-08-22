package com.turjo.easysleep.commands;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command executor for the /sleepgui command
 * Opens the comprehensive GUI interface
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class SleepGUICommand implements CommandExecutor, TabCompleter {
    
    private final EasySleep plugin;
    
    public SleepGUICommand(EasySleep plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check permissions
        if (!player.hasPermission("easysleep.gui")) {
            MessageUtils.sendMessage(player, "&cYou don't have permission to use the GUI!");
            return true;
        }
        
        // Check anti-spam
        if (!plugin.getAntiSpamManager().canUseCommand(player)) {
            long cooldown = plugin.getAntiSpamManager().getCommandCooldown(player);
            MessageUtils.sendMessage(player, "&cPlease wait " + cooldown + " seconds before opening the GUI again!");
            return true;
        }
        
        // Handle subcommands
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            
            switch (subCommand) {
                case "reload":
                    if (!player.hasPermission("easysleep.admin")) {
                        MessageUtils.sendMessage(player, "&cYou don't have permission to reload GUI configuration!");
                        return true;
                    }
                    
                    plugin.getGUIConfigManager().reloadConfig();
                    plugin.getSectionConfigManager().reloadConfig();
                    MessageUtils.sendMessage(player, "&a✓ GUI configuration reloaded successfully!");
                    return true;
                    
                case "sections":
                    if (!player.hasPermission("easysleep.admin")) {
                        MessageUtils.sendMessage(player, "&cYou don't have permission to view section information!");
                        return true;
                    }
                    
                    showSectionInfo(player);
                    return true;
                    
                case "help":
                    showHelpMessage(player);
                    return true;
                    
                default:
                    MessageUtils.sendMessage(player, "&cUnknown subcommand: " + subCommand);
                    showHelpMessage(player);
                    return true;
            }
        }
        
        // Open main GUI
        plugin.getSleepGUI().openMainMenu(player);
        
        return true;
    }
    
    /**
     * Show section information
     */
    private void showSectionInfo(Player player) {
        MessageUtils.sendMessage(player, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(player, "&6║ &b📋 &f&lGUI SECTION STATUS &b📋 &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        
        for (String section : plugin.getSectionConfigManager().getAllSections()) {
            boolean enabled = plugin.getSectionConfigManager().isSectionEnabled(section);
            String status = enabled ? "&aEnabled" : "&cDisabled";
            String name = section.replace("-", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            
            MessageUtils.sendMessage(player, "&6║ &f" + String.format("%-20s", name) + " " + status + " &6║");
        }
        
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &7Edit sections in &esections.yml &6║");
        MessageUtils.sendMessage(player, "&6╚═══════════════════════════════════════════╝");
    }
    
    /**
     * Show help message
     */
    private void showHelpMessage(Player player) {
        MessageUtils.sendMessage(player, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(player, "&6║ &b🎮 &f&lSLEEPGUI COMMAND HELP &b🎮 &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &e/sleepgui &7- Open the main GUI menu     &6║");
        MessageUtils.sendMessage(player, "&6║ &e/sleepgui reload &7- Reload GUI config    &6║");
        MessageUtils.sendMessage(player, "&6║ &e/sleepgui sections &7- View section info  &6║");
        MessageUtils.sendMessage(player, "&6║ &e/sleepgui help &7- Show this help        &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &7Aliases: &e/sgui, /sleepui, /esleep     &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &7Configure GUI in &egui.yml &6║");
        MessageUtils.sendMessage(player, "&6║ &7Configure sections in &esections.yml &6║");
        MessageUtils.sendMessage(player, "&6╚═══════════════════════════════════════════╝");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("easysleep.gui")) {
            return completions;
        }
        
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> subCommands = Arrays.asList("reload", "sections", "help");
            
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(partial)) {
                    completions.add(subCommand);
                }
            }
        }
        
        return completions;
    }
}