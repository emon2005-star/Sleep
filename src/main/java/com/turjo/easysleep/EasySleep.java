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
import com.turjo.easysleep.managers.RewardsManager;
import com.turjo.easysleep.managers.QuantumSleepManager;
import com.turjo.easysleep.managers.DimensionalSleepManager;
import com.turjo.easysleep.managers.SleepAchievementManager;
import com.turjo.easysleep.managers.SleepEconomyManager;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * EasySleep Plugin Main Class
 * 
 * A Minecraft plugin that provides easy management of the playersSleepingPercentage
 * game rule with rewards, effects, and comprehensive sleep management.
 * 
 * @author Turjo
 * @version 1.5.2
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
    private RewardsManager rewardsManager;
    private QuantumSleepManager quantumSleepManager;
    private DimensionalSleepManager dimensionalSleepManager;
    private SleepAchievementManager sleepAchievementManager;
    private SleepEconomyManager sleepEconomyManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.rewardsManager = new RewardsManager(this);
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
        this.quantumSleepManager = new QuantumSleepManager(this);
        this.dimensionalSleepManager = new DimensionalSleepManager(this);
        this.sleepAchievementManager = new SleepAchievementManager(this);
        this.sleepEconomyManager = new SleepEconomyManager(this);
        
        // Initialize commands
        initializeCommands();
        
        // Register event listeners
        registerEventListeners();
        
        // Set default sleep percentage for all worlds
        setDefaultSleepPercentage();
        
        // Start update checker
        updateChecker.startPeriodicCheck();
        
        // Enhanced startup message
        getLogger().info("╔═══════════════════════════════════════════╗");
        getLogger().info("║       🌙 EASYSLEEP v1.5.2 ACTIVATED 🌙    ║");
        getLogger().info("║                                           ║");
        getLogger().info("║ 💬 Customizable Messages: FULL CONTROL   ║");
        getLogger().info("║ 🎛️ Message Categories: ENABLE/DISABLE    ║");
        getLogger().info("║ 🌌 Quantum Entanglement: REVOLUTIONARY   ║");
        getLogger().info("║ 🌍 Dimensional Portals: CROSS-WORLD      ║");
        getLogger().info("║ 🏆 Achievement System: 16 EXCLUSIVE      ║");
        getLogger().info("║ 💎 Dream Coin Economy: UNIQUE CURRENCY   ║");
        getLogger().info("║ 🎁 Rewards System: ACTIVE                ║");
        getLogger().info("║ ⚡ Time Acceleration: 1.5-2x SPEED       ║");
        getLogger().info("║ 🎨 Gentle Animations: OPTIMIZED         ║");
        getLogger().info("║ 🌅 Day-Night Cycle: STUNNING             ║");
        getLogger().info("║ 📊 Statistics Tracking: COMPREHENSIVE    ║");
        getLogger().info("║ 🤖 AFK Detection: INTELLIGENT            ║");
        getLogger().info("║ 🌙 Moon Phases: MYSTICAL BONUSES         ║");
        getLogger().info("║ 🏆 Achievement System: UNLOCKED          ║");
        getLogger().info("║ 💰 Economy Integration: VAULT READY      ║");
        getLogger().info("║ 🎮 Modern GUI: FULLY FUNCTIONAL          ║");
        getLogger().info("║ 🚀 Status: ULTIMATE SLEEP EXPERIENCE     ║");
        getLogger().info("║ 🔧 Multiverse Compatible: FIXED v1.5.2   ║");
        getLogger().info("╚═══════════════════════════════════════════╝");
    }
    
    @Override
    public void onDisable() {
        // Enhanced shutdown message
        getLogger().info("╔═══════════════════════════════════════════╗");
        getLogger().info("║     🌙 EASYSLEEP v1.5.3 DEACTIVATED 🌙    ║");
        getLogger().info("║                                           ║");
        getLogger().info("║ 🌌 Quantum states: COLLAPSED             ║");
        getLogger().info("║ 🌍 Dimensional portals: CLOSED           ║");
        getLogger().info("║ 🏆 Achievements: SAVED                   ║");
        getLogger().info("║ 💎 Dream economy: SECURED                ║");
        getLogger().info("║ 🎁 Rewards saved and secured             ║");
        getLogger().info("║ ⚡ All systems: OFFLINE                  ║");
        getLogger().info("║ 💫 Thanks for using EasySleep v1.5.3!    ║");
        getLogger().info("║ 📊 Statistics: SAVED                     ║");
        getLogger().info("╚═══════════════════════════════════════════╝");
        
        // Cleanup
        if (rewardsManager != null) {
            rewardsManager.cleanup();
        }
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
        if (quantumSleepManager != null) {
            quantumSleepManager.cleanup();
        }
        if (dimensionalSleepManager != null) {
            dimensionalSleepManager.cleanup();
        }
        if (sleepAchievementManager != null) {
            sleepAchievementManager.cleanup();
        }
        if (sleepEconomyManager != null) {
            sleepEconomyManager.cleanup();
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
        int defaultPercentage = configManager.getConfig().getInt("sleep.default-percentage", 50);
        for (World world : getServer().getWorlds()) {
            Integer currentPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
            if (currentPercentage == null || currentPercentage != defaultPercentage) {
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
     * Get the rewards manager
     * @return RewardsManager instance
     */
    public RewardsManager getRewardsManager() {
        return rewardsManager;
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
    
    /**
     * Get the quantum sleep manager
     * @return QuantumSleepManager instance
     */
    public QuantumSleepManager getQuantumSleepManager() {
        return quantumSleepManager;
    }
    
    /**
     * Get the dimensional sleep manager
     * @return DimensionalSleepManager instance
     */
    public DimensionalSleepManager getDimensionalSleepManager() {
        return dimensionalSleepManager;
    }
    
    /**
     * Get the sleep achievement manager
     * @return SleepAchievementManager instance
     */
    public SleepAchievementManager getSleepAchievementManager() {
        return sleepAchievementManager;
    }
    
    /**
     * Get the sleep economy manager
     * @return SleepEconomyManager instance
     */
    public SleepEconomyManager getSleepEconomyManager() {
        return sleepEconomyManager;
    }
}