package com.turjo.easysleep.commands;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command executor for the /sleep command
 * 
 * Handles all sleep management commands with advanced features,
 * proper validation, and futuristic user interface.
 * 
 * @author Turjo
 * @version 1.5.0
 */
public class SleepCommand implements CommandExecutor, TabCompleter {
    
    private final EasySleep plugin;
    
    public SleepCommand(EasySleep plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permissions
        if (!sender.hasPermission("easysleep.admin")) {
            MessageUtils.sendMessage(sender, "&cYou don't have permission to use this command!");
            return true;
        }
        
        // Check anti-spam for players
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.getAntiSpamManager().canUseCommand(player)) {
                long cooldown = plugin.getAntiSpamManager().getCommandCooldown(player);
                MessageUtils.sendMessage(sender, "&cPlease wait " + cooldown + " seconds before using this command again!");
                return true;
            }
        }
        
        // Handle different argument lengths
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "set":
                return handleSetCommand(sender, args);
            case "get":
                return handleGetCommand(sender);
            case "status":
                return handleStatusCommand(sender);
            case "reset":
                return handleResetCommand(sender);
            case "reload":
                return handleReloadCommand(sender);
            case "setday":
                return handleSetDayCommand(sender, args);
            case "resetday":
                return handleResetDayCommand(sender);
            case "stats":
                return handleStatsCommand(sender);
            case "update":
                return handleUpdateCommand(sender);
            case "rewards":
                return handleRewardsCommand(sender);
            case "achievements":
                return handleAchievementsCommand(sender);
            case "shop":
                return handleShopCommand(sender, args);
            case "balance":
                return handleBalanceCommand(sender);
            case "help":
                sendHelpMessage(sender);
                return true;
            default:
                MessageUtils.sendMessage(sender, "&cUnknown subcommand: " + subCommand);
                sendHelpMessage(sender);
                return true;
        }
    }
    
    /**
     * Handle the set subcommand
     */
    private boolean handleSetCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /sleep set <percentage>");
            return true;
        }
        
        // Parse percentage value
        int percentage;
        try {
            percentage = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(sender, "&cInvalid number: " + args[1]);
            return true;
        }
        
        // Validate percentage range
        if (percentage < 0 || percentage > 100) {
            MessageUtils.sendMessage(sender, "&cPercentage must be between 0 and 100!");
            return true;
        }
        
        // Get the world to apply the game rule to
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        // Set the game rule
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentage);
        
        // Send success message
        MessageUtils.sendMessage(sender, "&aSuccessfully set sleep percentage to &e" + percentage + "% &ain world &e" + world.getName());
        
        // Log the change
        plugin.getLogger().info(sender.getName() + " set sleep percentage to " + percentage + "% in world " + world.getName());
        
        return true;
    }
    
    /**
     * Handle the get subcommand
     */
    private boolean handleGetCommand(CommandSender sender) {
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        if (currentPercentage == null) {
            MessageUtils.sendMessage(sender, "&cCould not retrieve sleep percentage!");
            return true;
        }
        
        MessageUtils.sendMessage(sender, "&aCurrent sleep percentage in &e" + world.getName() + "&a: &e" + currentPercentage + "%");
        return true;
    }
    
    /**
     * Handle the status subcommand
     */
    private boolean handleStatusCommand(CommandSender sender) {
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        // Get current statistics
        Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int totalPlayers = world.getPlayers().size();
        int sleepingPlayers = (int) world.getPlayers().stream()
                .mapToInt(p -> p.isSleeping() ? 1 : 0)
                .sum();
        
        MessageUtils.sendMessage(sender, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(sender, "&6║ &b⚡ &f&lSLEEP PROTOCOL STATUS &b⚡ &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &fWorld: &e" + world.getName() + String.format("%" + (28 - world.getName().length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fSleep Percentage: &a" + (currentPercentage != null ? currentPercentage + "%" : "Unknown") + String.format("%" + (18 - String.valueOf(currentPercentage != null ? currentPercentage : 0).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fOnline Players: &e" + totalPlayers + String.format("%" + (20 - String.valueOf(totalPlayers).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fCurrently Sleeping: &b" + sleepingPlayers + String.format("%" + (16 - String.valueOf(sleepingPlayers).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fAFK Players: &c" + plugin.getAFKManager().getAFKCount() + String.format("%" + (22 - String.valueOf(plugin.getAFKManager().getAFKCount()).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fTime: &e" + getTimeString(world.getTime()) + String.format("%" + (25 - getTimeString(world.getTime()).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fStatus: " + (world.getTime() > 12000 ? "&cNight" : "&aDay") + String.format("%" + (26 - (world.getTime() > 12000 ? "Night" : "Day").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fAnimations: " + (plugin.getConfigManager().areAnimationsEnabled() ? "&aEnabled" : "&cDisabled") + String.format("%" + (19 - (plugin.getConfigManager().areAnimationsEnabled() ? "Enabled" : "Disabled").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fDay Counter: " + (plugin.getConfigManager().isDayCounterEnabled() ? "&aDay " + plugin.getDayCounterManager().getCurrentDay(world) : "&cDisabled") + String.format("%" + (15 - (plugin.getConfigManager().isDayCounterEnabled() ? ("Day " + plugin.getDayCounterManager().getCurrentDay(world)) : "Disabled").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fEnhanced FX: " + (plugin.getConfigManager().areEnhancedParticlesEnabled() ? "&aActive" : "&cInactive") + String.format("%" + (18 - (plugin.getConfigManager().areEnhancedParticlesEnabled() ? "Active" : "Inactive").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fRewards: " + (plugin.getConfigManager().getConfig().getBoolean("rewards.enabled", true) ? "&aEnabled" : "&cDisabled") + String.format("%" + (20 - (plugin.getConfigManager().getConfig().getBoolean("rewards.enabled", true) ? "Enabled" : "Disabled").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fMoon Phase: " + plugin.getMoonPhaseManager().getMoonPhaseInfo(world) + String.format("%" + (20 - plugin.getMoonPhaseManager().getMoonPhaseInfo(world).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fDream States: &d" + plugin.getDreamSequenceManager().getActiveDreamCount() + String.format("%" + (18 - String.valueOf(plugin.getDreamSequenceManager().getActiveDreamCount()).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fActive Rituals: &5" + plugin.getSleepRitualManager().getActiveRitualCount() + String.format("%" + (16 - String.valueOf(plugin.getSleepRitualManager().getActiveRitualCount()).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &7⚡ All exclusive systems operational     &6║");
        MessageUtils.sendMessage(sender, "&6╚═══════════════════════════════════════════╝");
        
        return true;
    }
    
    /**
     * Convert world time to readable string
     */
    private String getTimeString(long time) {
        if (time >= 0 && time < 6000) {
            return "Morning";
        } else if (time >= 6000 && time < 12000) {
            return "Day";
        } else if (time >= 12000 && time < 18000) {
            return "Evening";
        } else {
            return "Night";
        }
    }
    
    /**
     * Handle the reset subcommand
     */
    private boolean handleResetCommand(CommandSender sender) {
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        // Reset to 1% (optimal for most servers)
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 1);
        
        MessageUtils.sendMessage(sender, "&a✓ &fSleep percentage &areset to optimal value &e(1%) &ain world &e" + world.getName());
        plugin.getLogger().info(sender.getName() + " reset sleep percentage to 1% in world " + world.getName());
        
        return true;
    }
    
    /**
     * Handle the reload subcommand
     */
    private boolean handleReloadCommand(CommandSender sender) {
        try {
            plugin.getConfigManager().reloadConfig();
            MessageUtils.sendMessage(sender, "&a✓ &fConfiguration &areloaded successfully&f!");
            plugin.getLogger().info(sender.getName() + " reloaded EasySleep configuration");
        } catch (Exception e) {
            MessageUtils.sendMessage(sender, "&c✗ &fFailed to reload configuration: " + e.getMessage());
            plugin.getLogger().warning("Failed to reload configuration: " + e.getMessage());
        }
        return true;
    }
    
    /**
     * Handle the setday subcommand
     */
    private boolean handleSetDayCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /sleep setday <day>");
            return true;
        }
        
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        try {
            long day = Long.parseLong(args[1]);
            if (day < 1) {
                MessageUtils.sendMessage(sender, "&cDay must be 1 or higher!");
                return true;
            }
            
            plugin.getDayCounterManager().setDay(world, day);
            MessageUtils.sendMessage(sender, "&a✓ &fSet day counter to &eDay " + day + " &fin world &e" + world.getName());
            plugin.getLogger().info(sender.getName() + " set day counter to " + day + " in world " + world.getName());
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(sender, "&cInvalid day number: " + args[1]);
        }
        
        return true;
    }
    
    /**
     * Handle the resetday subcommand
     */
    private boolean handleResetDayCommand(CommandSender sender) {
        World world = getTargetWorld(sender);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cCould not determine target world!");
            return true;
        }
        
        plugin.getDayCounterManager().resetDay(world);
        MessageUtils.sendMessage(sender, "&a✓ &fReset day counter to &eDay 1 &fin world &e" + world.getName());
        plugin.getLogger().info(sender.getName() + " reset day counter in world " + world.getName());
        
        return true;
    }
    
    /**
     * Handle the stats subcommand
     */
    private boolean handleStatsCommand(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(sender, "&6║ &b📊 &f&lEASYSLEEP STATISTICS &b📊 &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &fSleep Events: &e" + plugin.getStatisticsManager().getTotalSleepEvents() + " &6║");
        MessageUtils.sendMessage(sender, "&6║ &fNight Skips: &a" + plugin.getStatisticsManager().getTotalNightSkips() + " &6║");
        MessageUtils.sendMessage(sender, "&6║ &fDays Tracked: &b" + plugin.getStatisticsManager().getTotalDaysTracked() + " &6║");
        MessageUtils.sendMessage(sender, "&6║ &fPlayers Served: &d" + plugin.getStatisticsManager().getTotalPlayersServed() + " &6║");
        MessageUtils.sendMessage(sender, "&6║ &fVersion: &e" + plugin.getDescription().getVersion() + " &6║");
        MessageUtils.sendMessage(sender, "&6╚═══════════════════════════════════════════╝");
        return true;
    }
    
    /**
     * Handle the update subcommand
     */
    private boolean handleUpdateCommand(CommandSender sender) {
        plugin.getUpdateChecker().checkForUpdates();
        MessageUtils.sendMessage(sender, "&a✓ &fChecking for updates...");
        return true;
    }
    
    /**
     * Handle the rewards subcommand
     */
    private boolean handleRewardsCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        int streak = plugin.getRewardsManager().getPlayerStreak(player);
        
        MessageUtils.sendMessage(sender, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(sender, "&6║ &b🎁 &f&lREWARDS INFORMATION &b🎁 &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &fYour Sleep Streak: &e" + streak + " days" + String.format("%" + (15 - String.valueOf(streak).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fMoney per Sleep: &a$" + plugin.getConfigManager().getConfig().getDouble("rewards.economy.money-per-sleep", 25.0) + String.format("%" + (15 - String.valueOf(plugin.getConfigManager().getConfig().getDouble("rewards.economy.money-per-sleep", 25.0)).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fXP per Sleep: &b" + plugin.getConfigManager().getConfig().getInt("rewards.experience.xp-per-sleep", 100) + " XP" + String.format("%" + (15 - String.valueOf(plugin.getConfigManager().getConfig().getInt("rewards.experience.xp-per-sleep", 100)).length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6║ &fRewards Status: " + (plugin.getConfigManager().getConfig().getBoolean("rewards.enabled", true) ? "&aEnabled" : "&cDisabled") + String.format("%" + (17 - (plugin.getConfigManager().getConfig().getBoolean("rewards.enabled", true) ? "Enabled" : "Disabled").length()) + "s", "") + "&6║");
        MessageUtils.sendMessage(sender, "&6╚═══════════════════════════════════════════╝");
        return true;
    }
    
    /**
     * Handle the achievements subcommand
     */
    private boolean handleAchievementsCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        plugin.getSleepAchievementManager().showAchievements(player);
        return true;
    }
    
    /**
     * Handle the shop subcommand
     */
    private boolean handleShopCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length >= 3 && "buy".equals(args[1])) {
            String itemId = args[2];
            boolean success = plugin.getSleepEconomyManager().purchaseShopItem(player, itemId);
            if (!success) {
                MessageUtils.sendMessage(player, "&cPurchase failed! Check item name and balance.");
            }
        } else {
            plugin.getSleepEconomyManager().showSleepShop(player);
        }
        return true;
    }
    
    /**
     * Handle the balance subcommand
     */
    private boolean handleBalanceCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        long balance = plugin.getSleepEconomyManager().getDreamCoins(player);
        
        MessageUtils.sendMessage(player, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(player, "&6║ &d💎 &f&lDREAM COIN BALANCE &d💎 &6║");
        MessageUtils.sendMessage(player, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(player, "&6║ &fYour Balance: &e" + balance + " &dDream Coins &6║");
        MessageUtils.sendMessage(player, "&6║ &7Use: &e/sleep shop &7to browse items &6║");
        MessageUtils.sendMessage(player, "&6╚═══════════════════════════════════════════╝");
        return true;
    }
    
    /**
     * Get the target world for the command
     */
    private World getTargetWorld(CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getWorld();
        } else {
            // For console, use the default world
            return plugin.getServer().getWorlds().get(0);
        }
    }
    
    /**
     * Send help message to the command sender
     */
    private void sendHelpMessage(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6╔═══════════════════════════════════════════╗");
        MessageUtils.sendMessage(sender, "&6║ &b⚡ &f&lEASYSLEEP COMMAND MATRIX &b⚡ &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep set <0-100> &7- Configure sleep %  &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep get &7- View current settings     &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep status &7- Live system monitor    &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep reset &7- Reset to optimal (1%)   &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep reload &7- Hot-reload config      &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep setday <X> &7- Set day counter     &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep resetday &7- Reset to Day 1        &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep stats &7- View plugin statistics  &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep rewards &7- View reward info       &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep achievements &7- View achievements  &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep shop &7- Browse Dream Coin shop   &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep balance &7- Check Dream Coins     &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep update &7- Check for updates      &6║");
        MessageUtils.sendMessage(sender, "&6║ &e/sleep help &7- Show this matrix         &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &7Aliases: &e/sleepmanager, /sleepmgr      &6║");
        MessageUtils.sendMessage(sender, "&6║ &7         &e/nightskip                    &6║");
        MessageUtils.sendMessage(sender, "&6╠═══════════════════════════════════════════╣");
        MessageUtils.sendMessage(sender, "&6║ &7EasySleep &ev" + plugin.getDescription().getVersion() + " &7by &eTurjo              &6║");
        MessageUtils.sendMessage(sender, "&6╚═══════════════════════════════════════════╝");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("easysleep.admin")) {
            return completions;
        }
        
        if (args.length == 1) {
            // First argument - subcommands
            String partial = args[0].toLowerCase();
            List<String> subCommands = Arrays.asList("set", "get", "status", "reset", "reload", "setday", "resetday", "stats", "rewards", "achievements", "shop", "balance", "update", "help");
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(partial)) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("shop")) {
            // Shop subcommands
            String partial = args[1].toLowerCase();
            List<String> shopCommands = Arrays.asList("buy");
            for (String shopCommand : shopCommands) {
                if (shopCommand.startsWith(partial)) {
                    completions.add(shopCommand);
                }
            }
        } else if (args.length == 2) {
            // Second argument for 'set' command - percentage suggestions
            if (args[0].equalsIgnoreCase("set")) {
                String partial = args[1];
                List<String> suggestions = Arrays.asList("1", "10", "25", "50", "75", "100");
                for (String suggestion : suggestions) {
                    if (suggestion.startsWith(partial)) {
                        completions.add(suggestion);
                    }
                }
            } else if (args[0].equalsIgnoreCase("setday")) {
                String partial = args[1];
                List<String> suggestions = Arrays.asList("1", "10", "50", "100", "365");
                for (String suggestion : suggestions) {
                    if (suggestion.startsWith(partial)) {
                        completions.add(suggestion);
                    }
                }
            } else if (args[0].equalsIgnoreCase("shop") && args.length == 3 && "buy".equals(args[1])) {
                String partial = args[2];
                for (String item : plugin.getSleepEconomyManager().getShopItems()) {
                    if (item.startsWith(partial)) {
                        completions.add(item);
                    }
                }
            }
        }
        
        return completions;
    }
}