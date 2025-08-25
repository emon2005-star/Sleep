package com.turjo.easysleep;

import com.turjo.easysleep.commands.SleepCommand;
import com.turjo.easysleep.listeners.SleepEventListener;
import com.turjo.easysleep.listeners.PlayerEventListener;
import com.turjo.easysleep.managers.AnimationManager;
import com.turjo.easysleep.managers.AFKManager;
import com.turjo.easysleep.managers.AntiSpamManager;
import com.turjo.easysleep.managers.ClockAnimationManager;
import com.turjo.easysleep.managers.ConfigManager;
import com.turjo.easysleep.managers.DayCounterManager;
import com.turjo.easysleep.managers.DreamSequenceManager;
import com.turjo.easysleep.managers.DayNightCycleManager;
import com.turjo.easysleep.managers.MoonPhaseManager;
import com.turjo.easysleep.managers.SleepRitualManager;
import com.turjo.easysleep.managers.StatisticsManager;
import com.turjo.easysleep.managers.UpdateChecker;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * EasySleep Plugin Main Class
 * 
 * A Minecraft plugin that provides easy management of the playersSleepingPercentage
 * game rule through custom commands with stunning animations and modern interface.
 * 
 * @author Turjo
 * @version 1.4.1
 */
public class EasySleep extends JavaPlugin {
    
    private static EasySleep instance;
    private AnimationManager animationManager;
    private ConfigManager configManager;
    private DayCounterManager dayCounterManager;
    private UpdateChecker updateChecker;
    private AFKManager afkManager;
    private AntiSpamManager antiSpamManager;
    private ClockAnimationManager clockAnimationManager;
    private DayNightCycleManager dayNightCycleManager;
    private StatisticsManager statisticsManager;
    private DreamSequenceManager dreamSequenceManager;
    private SleepRitualManager sleepRitualManager;
    private MoonPhaseManager moonPhaseManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.animationManager = new AnimationManager(this);
        this.dayCounterManager = new DayCounterManager(this);
        this.updateChecker = new UpdateChecker(this);
        this.afkManager = new AFKManager(this);
        this.antiSpamManager = new AntiSpamManager(this);
        this.clockAnimationManager = new ClockAnimationManager(this);
        this.dayNightCycleManager = new DayNightCycleManager(this);
        this.statisticsManager = new StatisticsManager(this);
        this.dreamSequenceManager = new DreamSequenceManager(this);
        this.sleepRitualManager = new SleepRitualManager(this);
        this.moonPhaseManager = new MoonPhaseManager(this);
        
        // Initialize commands
        initializeCommands();
        
        // Register event listeners
        registerEventListeners();
        
        // Set default sleep percentage for all worlds
        setDefaultSleepPercentage();
        
        // Start update checker
        updateChecker.startPeriodicCheck();
        
        // Enhanced startup message with ASCII art
        getLogger().info("╔═══════════════════════════════════════════╗");
        getLogger().info("║       🌙 EASYSLEEP v1.4.1 ACTIVATED 🌙    ║");
        getLogger().info("║                                           ║");
        getLogger().info("║ ⚡ Sleep Protocol: ULTIMATE EDITION      ║");
        getLogger().info("║ 🌙 Night Skip System: ENHANCED           ║");
        getLogger().info("║ ✨ Animation Engine: OPTIMIZED           ║");
        getLogger().info("║ 🎵 Audio System: IMMERSIVE               ║");
        getLogger().info("║ 📅 Day Counter: ACTIVE                   ║");
        getLogger().info("║ 🔄 Update Checker: MONITORING            ║");
        getLogger().info("║ 👤 AFK Detection: ENABLED                ║");
        getLogger().info("║ 🕐 Clock Animation: RUNNING              ║");
        getLogger().info("║ 🌅 Day-Night Cycle: EPIC                 ║");
        getLogger().info("║ 🛡️ Anti-Spam: PROTECTED                  ║");
        getLogger().info("║ 🌙 Dream Sequences: EXCLUSIVE            ║");
        getLogger().info("║ 🔮 Sleep Rituals: REVOLUTIONARY          ║");
        getLogger().info("║ 🌕 Moon Phases: MYSTICAL                 ║");
        getLogger().info("║ 🚀 Status: READY FOR ACTION              ║");
        getLogger().info("╚═══════════════════════════════════════════╝");
    }
    
    @Override
    public void onDisable() {
        // Enhanced shutdown message
        getLogger().info("╔═══════════════════════════════════════════╗");
        getLogger().info("║     🌙 EASYSLEEP v1.4.1 DEACTIVATED 🌙    ║");
        getLogger().info("║                                           ║");
        getLogger().info("║ 🌙 Ultimate Sleep Protocol: TERMINATED   ║");
        getLogger().info("║ ⚡ All systems: OFFLINE                  ║");
        getLogger().info("║ 💫 Thanks for using EasySleep v1.4.1!    ║");
        getLogger().info("║ ✨ Animation threads: STOPPED            ║");
        getLogger().info("║ 📊 Statistics: SAVED                     ║");
        getLogger().info("╚═══════════════════════════════════════════╝");
        
        // Cleanup
        if (animationManager != null) {
            animationManager.cleanup();
        }
        if (dayCounterManager != null) {
            dayCounterManager.cleanup();
        }
        if (afkManager != null) {
            afkManager.cleanup();
        }
        if (antiSpamManager != null) {
            antiSpamManager.cleanup();
        }
        if (clockAnimationManager != null) {
            clockAnimationManager.cleanup();
        }
        if (dayNightCycleManager != null) {
            dayNightCycleManager.cleanup();
        }
        if (dreamSequenceManager != null) {
            dreamSequenceManager.cleanup();
        }
        if (sleepRitualManager != null) {
            sleepRitualManager.cleanup();
        }
        if (moonPhaseManager != null) {
            moonPhaseManager.cleanup();
        }
        instance = null;
    }
    
    /**
     * Initialize all plugin commands
     */
    private void initializeCommands() {
        SleepCommand sleepCommand = new SleepCommand(this);
        
        // Register main command and all aliases
        getCommand("sleep").setExecutor(sleepCommand);
        getCommand("sleep").setTabCompleter(sleepCommand);
        getCommand("sleepmanager").setExecutor(sleepCommand);
        getCommand("sleepmanager").setTabCompleter(sleepCommand);
        getCommand("sleepmgr").setExecutor(sleepCommand);
        getCommand("sleepmgr").setTabCompleter(sleepCommand);
        getCommand("nightskip").setExecutor(sleepCommand);
        getCommand("nightskip").setTabCompleter(sleepCommand);
    }
    
    /**
     * Register all event listeners
     */
    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new SleepEventListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }
    
    /**
     * Set default sleep percentage to 1% for all worlds
     */
    private void setDefaultSleepPercentage() {
        int defaultPercentage = configManager.getDefaultSleepPercentage();
        for (World world : getServer().getWorlds()) {
            Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
            if (currentPercentage == null || currentPercentage == 100) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, defaultPercentage);
                getLogger().info("Configured world '" + world.getName() + "' with " + defaultPercentage + "% sleep requirement");
            }
        }
    }
    
    /**
     * Get the plugin instance
     * @return EasySleep plugin instance
     */
    public static EasySleep getInstance() {
        return instance;
    }
    
    /**
     * Get the animation manager
     * @return AnimationManager instance
     */
    public AnimationManager getAnimationManager() {
        return animationManager;
    }
    
    /**
     * Get the config manager
     * @return ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Get the day counter manager
     * @return DayCounterManager instance
     */
    public DayCounterManager getDayCounterManager() {
        return dayCounterManager;
    }
    
    /**
     * Get the update checker
     * @return UpdateChecker instance
     */
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    
    /**
     * Get the AFK manager
     * @return AFKManager instance
     */
    public AFKManager getAFKManager() {
        return afkManager;
    }
    
    /**
     * Get the anti-spam manager
     * @return AntiSpamManager instance
     */
    public AntiSpamManager getAntiSpamManager() {
        return antiSpamManager;
    }
    
    /**
     * Get the clock animation manager
     * @return ClockAnimationManager instance
     */
    public ClockAnimationManager getClockAnimationManager() {
        return clockAnimationManager;
    }
    
    /**
     * Get the day-night cycle manager
     * @return DayNightCycleManager instance
     */
    public DayNightCycleManager getDayNightCycleManager() {
        return dayNightCycleManager;
    }
    
    /**
     * Get the statistics manager
     * @return StatisticsManager instance
     */
    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }
    
    /**
     * Get the dream sequence manager
     * @return DreamSequenceManager instance
     */
    public DreamSequenceManager getDreamSequenceManager() {
        return dreamSequenceManager;
    }
    
    /**
     * Get the sleep ritual manager
     * @return SleepRitualManager instance
     */
    public SleepRitualManager getSleepRitualManager() {
        return sleepRitualManager;
    }
    
    /**
     * Get the moon phase manager
     * @return MoonPhaseManager instance
     */
    public MoonPhaseManager getMoonPhaseManager() {
        return moonPhaseManager;
    }
}