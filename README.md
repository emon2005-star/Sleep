# 🌙 EasySleep Plugin v1.4.1

The Ultimate Minecraft Sleep Management Plugin - A comprehensive solution that transforms the sleeping experience with stunning animations, intelligent systems, and professional-grade features.

## Features

### 🌟 **Core Features**
- **🌙 Intelligent Sleep Management**: Advanced sleep percentage control with smart defaults
- **✨ Epic Night Skip Animations**: Cinematic visual effects for sleeping and awake players
- **🎵 Immersive Audio System**: Carefully crafted sound effects that enhance the experience
- **📅 Advanced Day Counter**: Automatic day tracking with random good morning messages
- **🔄 Automatic Update Checker**: Stay updated with the latest features from SpigotMC

### 🎨 **Visual & Audio Excellence**
- **🌅 Day-Night Cycle Animations**: Stunning dawn and dusk transition effects
- **🕐 Clock Animation System**: Beautiful floating clocks showing real game time
- **🎭 Configurable Particles**: Customize every visual effect from the config
- **🔊 Soft Audio Experience**: Relaxing sounds that won't annoy players
- **⚡ Performance Optimized**: Smooth animations with minimal server impact

### 🛡️ **Intelligent Systems**
- **👤 AFK Detection**: Smart player activity monitoring
- **🛡️ Anti-Spam Protection**: Prevents message and command flooding
- **📊 Statistics Tracking**: Comprehensive usage analytics
- **🌍 Multi-World Support**: Seamless operation across all worlds
- **⚙️ Hot Configuration Reload**: Update settings without server restart

### 🚀 **Professional Features**
- **🎯 Smart Tab Completion**: Intelligent command suggestions
- **🔧 Granular Permissions**: Fine-tuned access control
- **💬 Futuristic Chat Interface**: Immersive sci-fi themed messages
- **📈 Real-time Monitoring**: Live sleep status and world information
- **🎨 Fully Customizable**: Every aspect configurable via config.yml

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sleep set <0-100>` | Set the sleep percentage (0-100) | `easysleep.admin` |
| `/sleep get` | Get the current sleep percentage | `easysleep.admin` |
| `/sleep status` | View live sleep protocol information | `easysleep.admin` |
| `/sleep reset` | Reset to optimal 1% sleep requirement | `easysleep.admin` |
| `/sleep reload` | Hot-reload plugin configuration | `easysleep.admin` |
| `/sleep setday <X>` | Set day counter to specific day | `easysleep.admin` |
| `/sleep resetday` | Reset day counter to Day 1 | `easysleep.admin` |
| `/sleep stats` | View comprehensive plugin statistics | `easysleep.admin` |
| `/sleep update` | Check for plugin updates | `easysleep.admin` |
| `/sleep help` | Show complete command help | `easysleep.admin` |

### Command Aliases
- `/sleepmanager` - Alternative to `/sleep`
- `/sleepmgr` - Short alternative to `/sleep`
- `/nightskip` - Alternative to `/sleep`

## Permissions

- `easysleep.admin` - Allows access to all sleep management commands (default: op)
- `easysleep.*` - Grants all EasySleep permissions (default: op)

## Installation

1. Download the `EasySleep-1.3.1.jar` file
2. Place it in your server's `plugins` folder
3. Restart your server or use a plugin manager to load it
4. The plugin is ready to use!

## Usage Examples

```
/sleep set 50        # Require 50% of players to sleep
/sleep set 1         # Only 1 player needs to sleep (default)
/sleep set 100       # All players must sleep
/sleep get           # Check current setting
/sleep status        # View live system information
/sleep reset         # Reset to 1% (optimal setting)
/sleep reload        # Hot-reload configuration
/sleep setday 100    # Set to day 100
/sleep resetday      # Reset to day 1
/sleep stats         # View plugin statistics
/sleep update        # Check for updates
/nightskip status    # Using alias command
```

## 🎨 **Epic Animation System**

### **🌙 Sleep Animations**
- **Dreamy Particle Spirals**: Beautiful effects around sleeping players
- **Floating Stars**: Enchanting celestial particles
- **Dream Sequence**: Epic completion animations
- **🕐 Clock Display**: Real-time game clock above sleeping players

### **⚡ Awake Player Effects**
- **Mystical Auras**: Portal and witch spell effects
- **Time Distortion**: Reality-bending visual feedback
- **Atmospheric Particles**: Immersive environmental effects

### **🌅 Day-Night Cycle**
- **Time Acceleration Effects**: Stunning futuristic animations during night skip
- **Reality Distortion**: Epic time vortex and temporal stabilization effects
- **Performance Optimized**: Smart rendering based on player proximity
- **Immersive Audio**: Layered sound effects for complete immersion

### **🌍 World-Wide Spectacle**
- **Lightning Strikes**: Epic visual effects during night skip
- **Global Announcements**: Synchronized messaging system
- **Professional Broadcasting**: Cinematic status updates
## ⚙️ Configuration

The plugin includes an extensive `config.yml` file with complete customization:

```yaml
settings:
  default-sleep-percentage: 1        # Default for new worlds (1-100)
  enable-animations: true            # Toggle stunning animations
  enable-sound-effects: true         # Toggle immersive sounds
  broadcast-sleep-messages: true     # Toggle sleep broadcasts
  auto-configure-new-worlds: true    # Auto-setup new worlds
  reduced-sleep-messages: false      # Toggle message length

features:
  day-counter: true                  # Enable day counter
  enhanced-particles: true           # Enhanced particle effects
  weather-effects: false             # Weather during night skip
  update-checker: true               # Automatic update checking
  afk-detection: true                # AFK player monitoring
  afk-threshold: 5                   # AFK time in minutes
  clock-animation: true              # Clock display for sleepers
  day-night-animation: true          # Epic transition effects
  anti-spam: true                    # Spam protection system

advanced:
  animation-intensity: 2             # Animation intensity (1-3)
  sound-volume: 0.2                  # Sound volume multiplier (softer)
  max-animation-distance: 32         # Max distance for animations
  performance-mode: false            # Performance optimization
  debug-mode: false                  # Enable debug logging

anti-spam:
  sleep-message-cooldown: 5          # Sleep message cooldown (seconds)
  wake-message-cooldown: 3           # Wake message cooldown (seconds)
  command-cooldown: 2                # Command cooldown (seconds)
effects:
  particle-density: 1.0              # Particle density multiplier
  sleep-particle: "HEART"            # Sleeping player particles
  awake-particle: "ENCHANTMENT_TABLE" # Awake player particles
  sleep-sound: "BLOCK_AMETHYST_BLOCK_CHIME"      # Gentle sleep sounds
  night-skip-sound: "ENTITY_EXPERIENCE_ORB_PICKUP" # Soft night skip sounds
```

## Building from Source

This plugin uses Maven for dependency management and building.

```bash
mvn clean package
```

The compiled JAR will be available in the `target` directory.

## Requirements

- Minecraft Server 1.20.1+ (Spigot/Paper recommended)
- Java 8 or higher
- Recommended: Paper server for optimal particle effects

## Developer

**Turjo** - Plugin Developer

## Version

Current Version: **1.3.1**

### Changelog

**v1.3.1:**
- 🚀 Major performance optimizations and enhanced particle system
- 🎨 Fully configurable particles, sounds, and effects from config
- 📅 Advanced day counter with random good morning messages
- 🎯 New admin commands: `/sleep setday` and `/sleep resetday`
- 🔧 Enhanced futuristic chat messages with more immersive styling
- ⚡ Replaced harsh sounds with relaxing, ambient audio effects
- 🌟 Performance mode for servers requiring optimization
- 🎭 Enhanced particle effects with `SPELL_WITCH` replacing `WITCH`
- 📊 Improved status command with more detailed information
- 🛠️ Better error handling and configuration validation

**v1.2.1:**
- 🚀 Enhanced command system with improved aliases support
- ⚙️ Advanced configuration options with hot-reload capability
- 🎛️ Configurable animation intensity and sound volume
- 🛠️ Performance optimizations and memory leak fixes
- 📊 Improved statistics calculation and display
- 🔧 Better error handling and validation
- 🌟 Enhanced user interface with more detailed help system
- 🎯 Smarter tab completion with better suggestions
- 📝 Comprehensive code documentation and comments

**v1.1.1:**
- ✨ Added stunning night skip animations for all players
- 🎵 Implemented immersive sound effects system
- ⚙️ Added comprehensive configuration system
- 📊 Enhanced status command with live statistics
- 🔄 Added reload command for configuration changes
- 🌍 Improved multi-world support and auto-configuration
- 🚀 Upgraded to futuristic chat interface
- 🛠️ Performance optimizations and bug fixes
- 📝 Added command aliases for better accessibility

## Support

If you encounter any issues or have suggestions:
- Visit the SpigotMC resource page: https://www.spigotmc.org/resources/easysleep.127995/
- Use `/sleep update` to check for the latest version
- Enable debug mode in config.yml for troubleshooting
- The plugin automatically notifies admins of available updates

---

*EasySleep v1.4.1 - The Ultimate Sleep Management Plugin with Epic Animations, Intelligent Systems, and Professional-Grade Features! Transform your server's sleep experience today!*