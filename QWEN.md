# Country Sim - Android Text-Based Country Simulation Game

## Project Overview

**Country Sim** is a comprehensive text-based country simulation game built for Android using Kotlin. Players take on the role of a national leader, making decisions across multiple domains including economy, politics, technology, diplomacy, and social policy.

### Key Features

- **Turn-based gameplay** - Make decisions each turn and manage your nation's progress
- **Advanced economic system** - 20 industries, 17 resources, dynamic market conditions
- **Political simulation** - 5 political parties, law proposals, parliamentary voting, elections
- **Technology tree** - 30 technologies across 5 tiers and 12 categories
- **NPC advisors** - 12 unique characters with personalities, relationships, and quests
- **Regional management** - 6 unlockable regions with bonuses
- **Dynamic events** - Random events, crises, and world conditions
- **Dark/Light mode** - Automatic theme switching based on system settings

## Technology Stack

- **Language**: Kotlin
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)
- **Compile SDK**: 34
- **UI**: Material Design 3 with custom layouts
- **Architecture**: Single Activity with game state managers

### Dependencies

```kotlin
androidx.appcompat:appcompat:1.6.1
androidx.constraintlayout:constraintlayout:2.1.4
com.google.android.material:material:1.9.0
androidx.startup:startup-runtime:1.1.1
```

## Project Structure

```
app/src/main/java/com/example/myapplication6/
├── Country.kt              # Player's country data model
├── CountrySimActivity.kt   # Main activity and UI controller
├── EconomyManager.kt       # Economic simulation (industries, resources, trade)
├── EventManager.kt         # Random event system
├── GameWorld.kt           # World state (regions, factions, diplomacy)
├── NPCManager.kt          # NPC advisors and relationships
├── PolicyManager.kt       # Political system (laws, parties, ministries)
└── TechnologyManager.kt   # Research and technology tree
```

## Building and Running

### Prerequisites

- Android Studio or compatible IDE
- JDK 17
- Android SDK with API 34

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires keystore configuration)
./gradlew assembleRelease

# Clean build
./gradlew clean

# Run tests (if available)
./gradlew test
```

### Debug APK Location

After building, the debug APK is located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions CI/CD

The project includes automated CI/CD via GitHub Actions:

- **Trigger**: Push to `main`/`master` or pull requests
- **Output**: Debug APK uploaded as artifact
- **Workflow**: `.github/workflows/debug-apk.yml`

## Game Systems

### Economy Manager
- 20 industries across 10 sectors
- 17 tradable resources with dynamic pricing
- Economic indicators (GDP, inflation, unemployment)
- Budget allocation system
- Market conditions (boom, recession, depression, etc.)

### Policy Manager
- 10 government ministries
- 5 political parties with ideologies
- Law proposal and voting system
- 6 lobby groups
- Election system every 20 turns

### Technology Manager
- 30 technologies in 5 tiers
- 4 research facilities
- 8 research projects
- Prerequisite-based progression

### NPC System
- 12 unique NPCs with personalities
- Relationship and mood tracking
- Quest system with rewards/consequences

## Development Conventions

### Code Style
- Kotlin idioms and best practices
- Data classes for game state
- Object singletons for managers
- Try-catch error handling in UI code

### Architecture Patterns
- Separation of concerns (data, logic, UI)
- Manager objects for game systems
- Serializable data models for state persistence

### UI/UX
- Material Design 3 components
- Dark/Light theme support via `values-night/`
- Simple LinearLayout-based layouts for performance
- Error toasts for debugging

## Current Branch

- **Main**: `main` - Stable releases
- **Development**: `feature/country-sim-game` - Active development

## Repository

- **GitHub**: https://github.com/lobbbin/Countrygame

## Game Objective

Survive as long as possible by managing:
- **Stability** - Keep above 0 to avoid collapse
- **Happiness** - Keep above 0 to prevent revolution
- **Treasury** - Avoid bankruptcy (below -$100M)

## Version Info

- **Version**: 2.0
- **Version Code**: 1
- **Application ID**: `com.example.myapplication6`
