# 🌍 Country Sim

**A deep text-based country simulation game for Android**

[![Build Status](https://github.com/lobbbin/Countrygame/actions/workflows/debug-apk.yml/badge.svg)](https://github.com/lobbbin/Countrygame/actions)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## 📖 About

Country Sim is a comprehensive text-based strategy game where you take on the role of a national leader. Every decision matters as you balance economy, politics, technology, diplomacy, and social welfare to keep your nation thriving.

Can you lead your country to prosperity, or will it collapse under your leadership?

---

## ✨ Features

### 🎮 Core Gameplay
- **Turn-based strategy** - Make critical decisions each turn
- **Dynamic events** - Respond to random crises and opportunities
- **Multiple victory conditions** - Survive and thrive as long as possible
- **Dark/Light mode** - Automatic theme switching

### 💰 Advanced Economy
- **20 industries** across 10 sectors (Agriculture, Manufacturing, Tech, Energy, etc.)
- **17 resources** with dynamic supply/demand pricing
- **Economic indicators** - GDP, inflation, unemployment, interest rates
- **Budget management** - Allocate funds across 12 categories
- **16 economic policies** - Tax cuts, quantitative easing, trade agreements, UBI
- **Market conditions** - Boom, recession, depression, stagflation, hyperinflation

### 🏛️ Political System
- **5 political parties** with different ideologies
- **Law proposal system** - Draft and pass legislation
- **Parliamentary voting** - Build coalitions and negotiate
- **10 government ministries** - Manage departments and projects
- **6 lobby groups** - Navigate special interests
- **Elections** - Face the voters every 20 turns

### 🔬 Technology Tree
- **30 technologies** across 5 tiers
- **12 research categories** - Military, Civilian, Medical, Computing, Space, etc.
- **4 research facilities** - Universities, labs, research centers
- **8 research projects** - Fund special initiatives
- **Prerequisite system** - Unlock advanced tech progressively

### 👥 NPC Advisors
- **12 unique characters** - Chief of Staff, General, Economist, Diplomat, etc.
- **Personality system** - Traits, motivations, fears, values
- **Relationship tracking** - Build alliances or make enemies
- **Quest system** - Complete missions for rewards
- **Dynamic advice** - Get counsel based on current situation

### 🗺️ World Management
- **6 regions** - Capital, Industrial, Agricultural, Coastal, Northern, Border
- **8 factions** - Political groups with varying power
- **Diplomatic relations** - Manage ties with 6 nations
- **World tension** - Global stability affects your nation
- **Threat levels** - Monitor national security

---

## 📸 Screenshots

*Game UI features a clean, Material Design 3 interface with:*
- National statistics dashboard
- Interactive event cards with multiple choices
- Economy management menus
- Policy and law proposal screens
- Technology research tree
- NPC advisor interactions

---

## 🚀 Installation

### Download
1. Visit the [Releases](https://github.com/lobbbin/Countrygame/releases) page
2. Download the latest `app-debug.apk`
3. Install on your Android device (enable "Install from Unknown Sources" if needed)

### Build from Source
```bash
# Clone the repository
git clone https://github.com/lobbbin/Countrygame.git
cd Countrygame

# Build debug APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Requirements
- **Android**: 8.0+ (API 26)
- **Storage**: ~50 MB
- **RAM**: 2 GB recommended

---

## 🎯 How to Play

### Basic Controls
1. **Next Turn** - Advance time and process all game systems
2. **Economy** - Manage industries, policies, budget, trade
3. **Policies** - Propose laws, view ministries, track polls
4. **Tech** - Research technologies and fund projects
5. **Advisors** - Interact with NPCs and accept quests
6. **World** - View global status and threats
7. **Regions** - Unlock and manage territories

### Game Stats
Monitor these key indicators:
- **Stability** - Keep above 0 or your government collapses
- **Happiness** - Keep above 0 or face revolution
- **Treasury** - Avoid bankruptcy (below -$100M)
- **GDP** - Economic output affects all systems
- **Military** - Defense capability
- **Education/Healthcare** - Long-term development
- **Infrastructure** - Enables growth
- **Environment** - Sustainability

### Tips for Success
- ✅ Balance your budget early
- ✅ Invest in education and healthcare
- ✅ Maintain good relations with advisors
- ✅ Research technologies strategically
- ✅ Watch for market conditions
- ✅ Build coalitions in parliament
- ✅ Unlock regions for bonuses
- ✅ Save before major decisions (manual save recommended)

---

## 🏗️ Architecture

```
app/src/main/java/com/example/myapplication6/
├── Country.kt              # Player nation data model
├── CountrySimActivity.kt   # Main activity & UI controller
├── EconomyManager.kt       # Economic simulation engine
├── EventManager.kt         # Random event system
├── GameWorld.kt           # World state manager
├── NPCManager.kt          # NPC advisor system
├── PolicyManager.kt       # Political & law system
└── TechnologyManager.kt   # Research & technology
```

### Tech Stack
- **Language**: Kotlin 1.9.22
- **UI**: Material Design 3 + Custom Layouts
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle 8.14.3

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow Kotlin coding conventions
- Add comments for complex logic
- Test your changes thoroughly
- Keep commits focused and descriptive

---

## 📝 Changelog

### Version 2.0 (Current)
- ✅ Added advanced economic management system
- ✅ Implemented political party and law system
- ✅ Created technology tree with 30 technologies
- ✅ Added 12 NPC advisors with quests
- ✅ Implemented regional management
- ✅ Added dark/light mode support
- ✅ Fixed Android 14 compatibility
- ✅ Improved UI responsiveness

### Version 1.0
- ✅ Basic turn-based gameplay
- ✅ Core stats system
- ✅ Random event system
- ✅ Simple UI

---

## 🐛 Known Issues

- Some complex calculations may cause brief UI delays
- Large economies may experience slight performance impact
- Save/load system planned for future update

**Found a bug?** Open an issue on the [Issues](https://github.com/lobbbin/Countrygame/issues) page.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Inspired by classic strategy games like *Democracy*, *NationStates*, and *Tropico*
- Built with [Material Design 3](https://m3.material.io/)
- Developed using [AndroidIDE](https://androidide.com/)

---

## 📬 Contact

- **Repository**: https://github.com/lobbbin/Countrygame
- **Issues**: https://github.com/lobbbin/Countrygame/issues
- **Discussions**: https://github.com/lobbbin/Countrygame/discussions

---

## 🎮 Ready to Lead?

**Download now** and test your leadership skills! Can you build a prosperous nation, or will history remember you as a failure?

```
   _____         _      __  __           _            
  / ____|       | |    |  \/  |         | |           
 | |     ___  __| | ___| \  / | ___   __| | ___ _ __  
 | |    / _ \/ _` |/ _ \ |\/| |/ _ \ / _` |/ _ \ '__| 
 | |___|  __/ (_| |  __/ |  | | (_) | (_| |  __/ |    
  \_____\___|\__,_|\___|_|  |_|\___/ \__,_|\___|_|    
                                                      
```

**Good luck, Mr. President! 🇺🇳**
