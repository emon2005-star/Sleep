# 🌙 EasySleep v1.5.0 - Ultimate Sleep Management Plugin

**Transform your Minecraft server's sleep experience with intelligent systems, stunning animations, rewards, and complete customization.**

## ✨ Key Features

### 🎯 **Smart Sleep System**
- **Default 50% requirement** - Balanced for all server sizes
- **Time acceleration** - Even 1 player sleeping speeds up time by 1.5-2x
- **AFK detection** - Automatically excludes inactive players
- **Multi-world support** - Independent settings per world
- **Live configuration** - Change settings without restart

### 🎁 **Rewards & Effects System**
- **Sleep rewards** - Money, XP, items for sleeping players
- **Daily bonuses** - Streak rewards for consecutive nights
- **Potion effects** - Beneficial effects for sleeping
- **Achievement system** - Unlock titles and special rewards
- **Moon phase bonuses** - Enhanced rewards during special phases

### 🎨 **Visual Excellence**
- **Gentle animations** - Subtle, beautiful particle effects
- **Day-night cycle** - Stunning time acceleration visualization
- **Immersive audio** - Carefully crafted ambient sounds
- **Clock display** - Elegant floating time indicators
- **Customizable effects** - Full control over all visuals

### 🖥️ **Modern GUI Interface**
- **Professional design** - Clean glass-panel interface
- **Live settings** - Real-time configuration management
- **Statistics dashboard** - Comprehensive usage analytics
- **Control panel** - Easy access to all features
- **Admin tools** - Advanced management options

## 🚀 Quick Setup

1. Download `EasySleep-1.5.0.jar`
2. Place in your `plugins` folder
3. Restart server
4. Configure via `/sleepgui` or edit `config.yml`
5. Enjoy enhanced sleep experience!

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sleep set <percentage>` | Set sleep percentage (1-100) | `easysleep.admin` |
| `/sleep get` | View current settings | `easysleep.admin` |
| `/sleep status` | Display comprehensive status | `easysleep.admin` |
| `/sleep reset` | Reset to default 50% | `easysleep.admin` |
| `/sleep reload` | Hot-reload configuration | `easysleep.admin` |
| `/sleepgui` | Open modern GUI interface | `easysleep.gui` |
| `/sleep rewards` | View available rewards | `easysleep.user` |
| `/sleep stats` | Personal statistics | `easysleep.user` |

**Aliases:** `/sleepmanager`, `/sleepmgr`, `/nightskip`, `/sgui`, `/sleepui`

## 🔑 Permissions

- `easysleep.admin` - Full administrative access (default: op)
- `easysleep.gui` - Access to GUI interface (default: true)
- `easysleep.user` - Basic user features (default: true)
- `easysleep.rewards` - Receive sleep rewards (default: true)
- `easysleep.*` - All permissions (default: op)

## ⚙️ Configuration Highlights

### Sleep System
```yaml
sleep:
  default-percentage: 50  # Default sleep requirement
  time-acceleration: 1.75  # Speed when anyone sleeps
  afk-detection: true
  thunderstorm-skip: true
```

### Rewards System
```yaml
rewards:
  enabled: true
  money-per-sleep: 25.0
  xp-per-sleep: 100
  items:
    - "GOLDEN_APPLE:1"
    - "EMERALD:2"
  effects:
    - "REGENERATION:30:1"
    - "SPEED:60:0"
```

### Visual Effects
```yaml
animations:
  enabled: true
  intensity: 2  # 1-3 scale
  gentle-mode: true
  day-night-cycle: true
  morning-effects: true
```

## 🎁 Rewards System

### Sleep Rewards
- **💰 Money** - Configurable Vault integration
- **⭐ Experience** - XP rewards for sleeping
- **🎒 Items** - Custom item rewards
- **🧪 Effects** - Beneficial potion effects
- **🏆 Titles** - Special display names

### Streak System
- **📅 Daily streaks** - Consecutive sleep bonuses
- **🌟 Milestones** - Special rewards at 7, 30, 100 days
- **👑 Achievements** - Unlock exclusive titles
- **💎 Multipliers** - Increased rewards for dedication

### Moon Phase Bonuses
- **🌕 Full Moon** - 2x rewards and special effects
- **🌑 New Moon** - Rare item chances
- **🌓 Quarter Moons** - Balanced bonuses
- **🌒🌘 Crescents** - Progressive effects

## 🌅 Daily Messages (100+ Inspirational)

Start each day with motivation:
- "Day %day% - Today is your canvas, paint it beautifully!"
- "Day %day% - Every sunrise is an invitation to brighten someone's day!"
- "Day %day% - Your potential is endless, your possibilities infinite!"
- "Day %day% - Dreams don't work unless you do - make today count!"
- "Day %day% - The universe conspires to help those who help themselves!"
- "Day %day% - Success is not final, failure is not fatal - courage continues!"
- "Day %day% - Be the reason someone believes in the goodness of people!"
- "Day %day% - Life is 10% what happens, 90% how you react to it!"
- "Day %day% - The best time to plant a tree was 20 years ago, second best is now!"
- "Day %day% - Your only limit is your mind - think bigger, dream larger!"

*...and 90+ more inspiring messages to brighten every morning!*

## 📊 Statistics & Analytics

Track comprehensive data:
- **Sleep events** - Total sleep sessions
- **Time saved** - Hours of night skipped
- **Rewards earned** - Money, XP, items received
- **Streak records** - Longest consecutive streaks
- **World statistics** - Per-world sleep data
- **Player rankings** - Top sleepers leaderboard

## 🎨 Customization Features

### Visual Effects
- **20+ particle types** - Choose your preferred effects
- **3 intensity levels** - From subtle to spectacular
- **Color customization** - Match your server theme
- **Sound library** - 15+ ambient sound options
- **Performance modes** - Optimized for all server sizes

### GUI Interface
- **Layout control** - Customize menu designs
- **Item customization** - Materials, names, descriptions
- **Sound feedback** - Audio for all interactions
- **Color schemes** - Full theme control
- **Section management** - Enable/disable features

## 🌍 Advanced Features

### Time Management
- **Smart acceleration** - 1.5-2x speed when anyone sleeps
- **Weather integration** - Skip storms automatically
- **World synchronization** - Coordinate multiple worlds
- **Time zones** - Different settings per world

### Player Management
- **AFK detection** - Exclude inactive players
- **Permission integration** - Role-based access
- **Statistics tracking** - Individual player data
- **Reward eligibility** - Configurable requirements

### Performance Optimization
- **Smart rendering** - Distance-based effects
- **Memory management** - Automatic cleanup
- **Lag prevention** - Built-in monitoring
- **Resource efficiency** - Optimized algorithms

## 🛠️ Developer API

```java
// Get sleep percentage
int percentage = EasySleep.getInstance().getSleepPercentage(world);

// Check reward eligibility
boolean eligible = EasySleep.getInstance().isEligibleForRewards(player);

// Give sleep rewards
EasySleep.getInstance().giveSleepRewards(player);

// Get player statistics
SleepStats stats = EasySleep.getInstance().getPlayerStats(player);
```

## 📈 Version History

### v1.5.0 (Current)
- 🎁 Complete rewards and effects system
- 🌟 100+ inspirational daily messages
- ⚡ Smart time acceleration (1.5-2x speed)
- 🎨 Gentle, customizable animations
- 📱 Modern GUI with full customization
- 🌙 Advanced moon phase system
- 🏆 Achievement and streak system
- 📊 Comprehensive statistics tracking
- 🌍 Enhanced multi-world support
- 🔧 Complete configuration control

### Previous Versions
- v1.4.1 - Dream sequences and rituals
- v1.3.1 - Day counter and audio system
- v1.2.0 - Animation system
- v1.1.0 - Basic GUI implementation
- v1.0.0 - Initial release

## 🆘 Support & Resources

- **SpigotMC**: https://www.spigotmc.org/resources/easysleep.127995/
- **Discord**: Join our community server
- **Documentation**: Complete setup guides
- **Video Tutorials**: Step-by-step configuration
- **Bug Reports**: GitHub issue tracker

## 📋 Requirements

- **Minecraft**: 1.20.1+ (Spigot/Paper recommended)
- **Java**: 8 or higher
- **Optional**: Vault (for economy rewards)
- **Optional**: PlaceholderAPI (for advanced placeholders)
- **Optional**: LuckPerms (for advanced permissions)

## 🌟 Why Choose EasySleep?

✅ **Feature-rich** - Most comprehensive sleep plugin available  
✅ **Performance optimized** - Minimal server impact  
✅ **Highly customizable** - Every aspect configurable  
✅ **Modern design** - Beautiful, intuitive interface  
✅ **Active development** - Regular updates and improvements  
✅ **Community driven** - Features requested by users  
✅ **Professional support** - Dedicated developer assistance  
✅ **Cross-version compatible** - Works on all modern Minecraft versions  

## 👨‍💻 Developer

**Turjo** - Senior Plugin Developer  
*Crafting the ultimate Minecraft sleep experience since 2024*

---

**EasySleep v1.5.0** - *Transform your server's sleep experience with intelligent systems, stunning visuals, and rewarding gameplay mechanics that keep players engaged and servers thriving!*

*"Sweet dreams are made of code, and we've coded the sweetest dreams for your Minecraft server!"* 🌙✨