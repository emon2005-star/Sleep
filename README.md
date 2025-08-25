# 🌙 EasySleep Plugin v1.5.0

**The Ultimate Minecraft Sleep Management Plugin** - Transform your server's sleep experience with intelligent systems, stunning animations, rewards, and complete customization.

## ✨ Features Overview

### 🌟 **Core Sleep Management**
- **🎯 Smart Sleep System**: Default 50% requirement with intelligent time acceleration
- **⚡ Time Acceleration**: Even 1 player sleeping speeds up time by 1.5-2x
- **🤖 AFK Detection**: Automatically excludes inactive players from sleep calculations
- **🌍 Multi-World Support**: Independent settings for each world
- **⚙️ Live Configuration**: Change settings without server restart

### 🎁 **Rewards & Effects System**
- **💰 Sleep Rewards**: Configurable money, XP, and items for sleeping
- **🌟 Daily Bonuses**: Special rewards for consecutive sleep streaks
- **🔮 Sleep Effects**: Beneficial potion effects for sleeping players
- **🏆 Achievement System**: Unlock titles and rewards based on sleep patterns
- **🌙 Moon Phase Bonuses**: Enhanced rewards during special lunar phases

### 🎨 **Visual & Audio Excellence**
- **✨ Gentle Animations**: Subtle, beautiful particle effects
- **🌅 Day-Night Cycle**: Stunning time acceleration visualization
- **🎵 Immersive Audio**: Carefully crafted ambient sounds
- **🕐 Clock Display**: Elegant floating time indicators
- **🌈 Customizable Effects**: Full control over all visual elements

### 🖥️ **Modern GUI Interface**
- **📱 Intuitive Design**: Professional glass-panel interface
- **⚙️ Live Settings**: Real-time configuration management
- **📊 Statistics Dashboard**: Comprehensive usage analytics
- **🎛️ Control Panel**: Easy access to all features
- **🔧 Admin Tools**: Advanced management options

### 🌍 **Advanced Features**
- **⛈️ Weather Control**: Skip thunderstorms during sleep
- **🌕 Lunar Cycles**: Moon phases affect sleep bonuses
- **🔮 Dream Sequences**: Exclusive immersive experiences
- **🎭 Sleep Rituals**: Group sleep ceremonies with special effects
- **📈 Performance Optimized**: Smart resource management

## 🚀 Quick Start

1. **Download** the latest `EasySleep-1.5.0.jar`
2. **Place** in your server's `plugins` folder
3. **Restart** your server
4. **Configure** via `/sleepgui` or edit `config.yml`
5. **Enjoy** the enhanced sleep experience!

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sleep set <percentage>` | Set sleep percentage (1-100) | `easysleep.admin` |
| `/sleep get` | View current sleep settings | `easysleep.admin` |
| `/sleep status` | Display comprehensive sleep status | `easysleep.admin` |
| `/sleep reset` | Reset to default 50% requirement | `easysleep.admin` |
| `/sleep reload` | Hot-reload all configurations | `easysleep.admin` |
| `/sleepgui` | Open modern GUI interface | `easysleep.gui` |
| `/sleep rewards` | View available sleep rewards | `easysleep.user` |
| `/sleep stats` | Personal sleep statistics | `easysleep.user` |

### Command Aliases
- `/sleepmanager`, `/sleepmgr`, `/nightskip` - Alternative to `/sleep`
- `/sgui`, `/sleepui`, `/esleep` - Alternative to `/sleepgui`

## 🔑 Permissions

- `easysleep.admin` - Full administrative access (default: op)
- `easysleep.gui` - Access to GUI interface (default: true)
- `easysleep.user` - Basic user features (default: true)
- `easysleep.rewards` - Receive sleep rewards (default: true)
- `easysleep.*` - All permissions (default: op)

## ⚙️ Configuration

### Main Settings (config.yml)
```yaml
# Sleep Requirements
sleep-percentage: 50  # Default percentage of players needed
time-acceleration: 1.75  # Speed multiplier when anyone sleeps

# Rewards System
rewards:
  enabled: true
  money-per-sleep: 10.0
  xp-per-sleep: 50
  streak-bonuses: true

# Visual Effects
animations:
  enabled: true
  intensity: 2  # 1-3 scale
  gentle-mode: true  # Subtle, non-intrusive effects
```

## 🎁 Rewards System

### Sleep Rewards
- **💰 Money**: Configurable amount per sleep
- **⭐ Experience**: XP rewards for sleeping
- **🎒 Items**: Custom item rewards
- **🧪 Effects**: Beneficial potion effects

### Streak Bonuses
- **📅 Daily Streaks**: Consecutive sleep rewards
- **🏆 Milestones**: Special rewards at 7, 30, 100 days
- **👑 Titles**: Unlock special display names
- **🌟 Multipliers**: Increased rewards for dedication

### Moon Phase Bonuses
- **🌕 Full Moon**: 2x rewards and special effects
- **🌑 New Moon**: Rare item chances
- **🌓 Quarter Moons**: Balanced bonuses
- **🌒🌘 Crescents**: Growing/diminishing effects

## 📊 Statistics Tracking

- **Sleep Events**: Total times slept
- **Night Skips**: Successful time accelerations  
- **Streak Records**: Longest consecutive sleep streaks
- **Rewards Earned**: Total money, XP, and items received
- **Time Saved**: Hours of night time skipped
- **World Statistics**: Per-world sleep data

## 🎨 Customization

### Visual Effects
- **Particle Types**: Choose from 20+ particle effects
- **Animation Intensity**: 3 levels of visual complexity
- **Color Schemes**: Customize particle colors
- **Sound Effects**: Full audio customization
- **Performance Mode**: Optimized for lower-end servers

### GUI Interface
- **Layout**: Customizable menu designs
- **Items**: Change materials, names, and descriptions
- **Sounds**: Configure all GUI audio feedback
- **Colors**: Full color scheme control
- **Sections**: Enable/disable specific features

## 🌅 Daily Messages

Inspiring messages to start each day:
- "Day %day% - A new adventure begins!"
- "Day %day% - The universe conspires to help you succeed!"
- "Day %day% - Today is full of infinite possibilities!"
- "Day %day% - Your dreams are the blueprints of your reality!"
- "Day %day% - Every sunrise brings new opportunities!"
- *...and 50+ more inspiring messages!*

## 🔧 Advanced Features

### Time Acceleration
- **Smart Detection**: Automatically speeds up time when players sleep
- **Configurable Speed**: 1.5x to 3x time acceleration
- **Visual Feedback**: Beautiful day-night cycle animations
- **Weather Integration**: Skip storms and rain

### AFK Management
- **Intelligent Detection**: Excludes inactive players
- **Configurable Threshold**: Set AFK timeout (default: 5 minutes)
- **Movement Tracking**: Monitors player activity
- **Fair Sleep Calculation**: Only counts active players

### Performance Optimization
- **Smart Rendering**: Distance-based effect culling
- **Resource Management**: Efficient particle systems
- **Memory Optimization**: Automatic cleanup systems
- **Lag Prevention**: Built-in performance monitoring

## 🌍 Multi-World Support

- **Independent Settings**: Each world has its own configuration
- **Auto-Configuration**: New worlds automatically configured
- **World-Specific Rewards**: Different rewards per world
- **Synchronized Statistics**: Global player statistics

## 🛠️ Developer API

```java
// Get sleep percentage for world
int percentage = EasySleep.getInstance().getSleepPercentage(world);

// Check if player is eligible for rewards
boolean eligible = EasySleep.getInstance().isEligibleForRewards(player);

// Give sleep rewards to player
EasySleep.getInstance().giveSleepRewards(player);
```

## 📈 Version History

### v1.5.0 (Latest)
- 🎁 Complete rewards and effects system
- 🌟 Enhanced daily messages (50+ inspirational quotes)
- ⚡ Smart time acceleration (1.5-2x speed)
- 🎨 Gentle, non-intrusive animations
- 📱 Modern GUI with full customization
- 🌙 Advanced moon phase system
- 🏆 Achievement and streak system
- 📊 Comprehensive statistics tracking

### v1.4.1
- 🔮 Dream sequences and sleep rituals
- 🌕 Moon phase bonuses
- 🤖 Advanced AFK detection
- 🛡️ Anti-spam protection

### v1.3.1
- 📅 Day counter system
- 🎵 Immersive audio system
- ⚙️ Hot configuration reload
- 🔄 Automatic update checker

## 🆘 Support & Updates

- **SpigotMC**: https://www.spigotmc.org/resources/easysleep.127995/
- **Auto-Updates**: Plugin checks for updates automatically
- **Debug Mode**: Enable in config for troubleshooting
- **Performance Monitoring**: Built-in lag detection

## 📋 Requirements

- **Minecraft**: 1.20.1+ (Spigot/Paper recommended)
- **Java**: 8 or higher
- **Optional**: Vault (for economy rewards)
- **Optional**: PlaceholderAPI (for advanced placeholders)

## 👨‍💻 Developer

**Turjo** - Plugin Developer  
*Creating the ultimate Minecraft sleep experience*

---

*EasySleep v1.5.0 - Transform your server's sleep experience with intelligent systems, beautiful animations, and rewarding gameplay mechanics!*