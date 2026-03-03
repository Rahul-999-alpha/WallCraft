# ClearWalls - AI-Powered Wallpaper Application

<div align="center">
  <img src="Clear walls A1.png" alt="ClearWalls Logo" width="200"/>

  [![Version](https://img.shields.io/badge/version-1.0.2-blue.svg)](https://github.com/Rahul-999-alpha/WallCraft/releases/tag/v1.0.2)
  [![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
  [![Android](https://img.shields.io/badge/Android-26%2B-brightgreen.svg)](https://developer.android.com)
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org)
</div>

## 📱 Overview

ClearWalls is a modern Android wallpaper application that combines curated wallpapers from multiple sources with AI-powered wallpaper generation. Built with Jetpack Compose and Material 3, it offers a clean, minimal, and elegant user experience.

### ✨ Key Features

- 🎨 **Multi-Source Wallpapers** - Browse wallpapers from Pixabay and Wallhaven
- 🤖 **AI Generation** - Create custom wallpapers using Stability AI
- 📱 **Quality Options** - Download in multiple resolutions (480p, 1080p, 2K, 4K)
- ❤️ **Favorites** - Save and organize your favorite wallpapers
- 🔍 **Smart Search** - Find wallpapers by keywords with debounced search
- 🎭 **Theme Modes** - Light, Dark, AMOLED (pure black), and System themes
- 📂 **Category Browse** - Explore curated categories (Nature, Abstract, Space, etc.)
- ⚡ **Auto Wallpaper Changer** - Automatically rotate wallpapers at set intervals
- 🎯 **Set Wallpaper** - Apply to home screen, lock screen, or both
- 💾 **Offline Support** - Access favorites without internet

### 🎯 Upcoming Features (Phase 2)

- 📸 Pexels, Unsplash, Pinterest, Freepik integration
- 🖼️ Native ad cards in grid
- 🔄 Auto-refresh with WorkManager
- 🔧 Hidden admin panel for configuration
- 📊 Enhanced analytics

---

## 🏗️ Architecture

ClearWalls follows **Clean Architecture** principles with clear separation of concerns:

```
app/
├── core/
│   ├── common/          # Constants, sealed classes
│   ├── util/            # Utilities (AdManager, Extensions)
│   └── di/              # Dependency Injection (Hilt modules)
├── data/
│   ├── local/           # Room database (entities, DAOs)
│   ├── remote/          # API interfaces and DTOs
│   ├── repository/      # Repository implementations
│   ├── mapper/          # Data mappers
│   └── paging/          # Paging 3 sources
├── domain/
│   ├── model/           # Domain models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic use cases
└── presentation/
    ├── components/      # Reusable UI components
    ├── navigation/      # Navigation setup
    ├── theme/           # Theme configuration
    └── [screens]/       # Feature screens with ViewModels
```

### 🛠️ Tech Stack

**Core:**
- Kotlin 1.9
- Android SDK 26+ (Target: 35)
- Jetpack Compose (BOM 2024.12.01)
- Material 3

**Architecture & DI:**
- MVVM Pattern
- Hilt (Dependency Injection)
- Coroutines & Flow
- StateFlow & SharedFlow

**Networking & Data:**
- Retrofit 2.11
- OkHttp 4.12 (with caching)
- Gson
- Room 2.6.1
- DataStore Preferences
- Paging 3

**Image Loading:**
- Coil 3.0.4 (Compose + OkHttp integration)

**Firebase:**
- Firestore (curated wallpapers)
- Storage (image hosting)
- Remote Config
- Crashlytics
- Analytics

**Monetization:**
- Google AdMob (Banner, Interstitial, Rewarded, Native, App Open)

**Background Work:**
- WorkManager 2.10.0 (auto wallpaper changer)

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35
- Gradle 8.x

### Installation

1. **Clone the repository:**
   ```bash
   git clone git@github.com-rahul:Rahul-999-alpha/WallCraft.git
   cd WallCraft
   ```

2. **Create `local.properties` in the project root:**
   ```properties
   # API Keys
   PIXABAY_API_KEY=your_pixabay_key
   WALLHAVEN_API_KEY=your_wallhaven_key
   STABILITY_AI_API_KEY=your_stability_ai_key

   # AdMob IDs (Production)
   ADMOB_APP_ID=ca-app-pub-xxxxx~xxxxxxxx
   ADMOB_BANNER_ID=ca-app-pub-xxxxx/xxxxxxxx
   ADMOB_INTERSTITIAL_ID=ca-app-pub-xxxxx/xxxxxxxx
   ADMOB_REWARDED_ID=ca-app-pub-xxxxx/xxxxxxxx
   ADMOB_NATIVE_ID=ca-app-pub-xxxxx/xxxxxxxx
   ADMOB_APP_OPEN_ID=ca-app-pub-xxxxx/xxxxxxxx

   # Future APIs (Phase 2)
   PEXELS_API_KEY=your_pexels_key
   UNSPLASH_ACCESS_KEY=your_unsplash_key
   PINTEREST_ACCESS_TOKEN=your_pinterest_token
   FREEPIK_API_KEY=your_freepik_key
   ```

3. **Add `google-services.json`:**
   - Download from Firebase Console
   - Place in `app/` directory

4. **Sync project with Gradle:**
   ```bash
   ./gradlew sync
   ```

5. **Build and run:**
   ```bash
   # Debug build (uses Google test ad IDs)
   ./gradlew assembleDebug

   # Release build (uses production ad IDs)
   ./gradlew assembleRelease
   ```

---

## 📦 Build Variants

### Debug
- App ID: `com.rahul.clearwalls.debug`
- Uses Google AdMob test ad unit IDs
- Debuggable enabled
- No obfuscation

### Release
- App ID: `com.rahul.clearwalls`
- Uses production ad unit IDs from `local.properties`
- Minification enabled (R8)
- ProGuard rules applied
- Requires signing configuration

---

## 🎨 Design System

### Color Palette
Inspired by the logo - purple gradient mountains with warm sunset tones:

- **Primary:** `#7C3AED` (Violet 600)
- **Secondary:** `#A78BFA` (Violet 400)
- **Tertiary:** `#F59E0B` (Amber 500)
- **Dark Surface:** `#0F0D1A` (deep purple-black)
- **AMOLED:** Pure black (`#000000`)

### Typography
- **Font Family:** Inter (Google Fonts)
- Modern, clean, highly readable
- Scales: Display, Headline, Body, Label

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Monitor Ad Loading (ADB Logcat)
```bash
adb logcat | grep AdManager
```

**Log Indicators:**
- 📢 = Ad loading started
- ✅ = Ad loaded successfully
- ❌ = Ad failed (includes error code)

---

## 📊 Ad Integration

ClearWalls uses Google AdMob with 5 ad formats:

| Ad Type | Placement | Frequency |
|---------|-----------|-----------|
| **Banner** | Bottom of Home/Browse/Favorites | Persistent |
| **Interstitial** | After wallpaper actions | Every 3rd download / 5th set |
| **Rewarded** | AI Generate (credits depleted) | On-demand |
| **Native** | Wallpaper grid | Every 12 items (Phase 2) |
| **App Open** | App resume | 4-hour cooldown, 10s grace |

### Ad Configuration Constants

See `core/common/Constants.kt`:
```kotlin
AD_INTERSTITIAL_SET_INTERVAL = 5
AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 3
AD_INTERSTITIAL_COOLDOWN_MS = 60_000L  // 1 minute
AD_FIRST_SESSION_GRACE_MS = 10_000L    // 10 seconds
```

---

## 📱 Screens

1. **Onboarding** - First-time user introduction (3 pages)
2. **Home** - Categories, search, wallpaper grid with pagination
3. **Browse** - Category-filtered wallpaper grid
4. **Favorites** - Saved wallpapers (offline access)
5. **AI Generate** - Stability AI wallpaper generation
6. **Wallpaper Detail** - Preview, zoom, download, set, share
7. **Settings** - Theme, image quality, data saver, auto wallpaper
8. **Admin Panel** - Hidden configuration (7 taps on logo + password)

---

## 🔑 API Integrations

### Current (v1.0.2)
- **Pixabay** - Free stock photos API
- **Wallhaven** - Wallpaper-specific API
- **Stability AI** - AI image generation (SDXL 1.0)

### Planned (Phase 2)
- **Pexels** - High-quality free photos
- **Unsplash** - Professional photography
- **Pinterest** - Curated pin images
- **Freepik** - Premium design resources

### Firebase
- **Firestore** - Curated wallpaper metadata
- **Storage** - Hosted wallpaper images
- **Remote Config** - Feature flags, dynamic content
- **Crashlytics** - Crash reporting
- **Analytics** - User behavior tracking

---

## 🔐 Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.SET_WALLPAPER" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## 📈 Performance Optimizations

- **Image Caching:** 250 MB Coil disk cache
- **HTTP Caching:** 50 MB OkHttp cache
- **Lazy Loading:** Paging 3 with configurable page size (20 items)
- **Debounced Search:** 500ms delay to reduce API calls
- **Connection Pooling:** Efficient HTTP connections
- **R8 Optimization:** 72% smaller APK than competitors (5.1 MB vs 18 MB)

---

## 🐛 Known Issues

See `ONGOING_FIXES.md` for detailed tracking.

### Current
- Native ad cards not yet implemented in UI (`NativeAdCard.kt` component)

### Fixed in v1.0.2
- ✅ Native ad wrong unit ID bug
- ✅ Added comprehensive ad logging
- ✅ Dynamic version display in Settings

---

## 📝 Changelog

### v1.0.2 (2026-03-02)
- **Fixed:** Native ad loading with correct ad unit ID
- **Added:** Comprehensive logging to AdManager with emoji indicators
- **Fixed:** Dynamic version display in Settings (uses BuildConfig)

### v1.0.1
- Initial public release
- Core features: Browse, Search, Favorites, AI Generate, Set Wallpaper
- Multi-source integration (Pixabay, Wallhaven)
- AdMob integration (Banner, Interstitial, Rewarded, App Open)

### v1.0.0
- Beta release for testing

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow the existing code style (see `CLAUDE.md`)
4. Write clear commit messages
5. Test thoroughly (debug + release builds)
6. Submit a pull request

### Code Standards
- Kotlin naming conventions (camelCase, PascalCase)
- Clean Architecture layers respected
- MVVM pattern for presentation layer
- Hilt for dependency injection
- Coroutines for async operations
- StateFlow for UI state

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Pixabay** - Free stock photos
- **Wallhaven** - Wallpaper community
- **Stability AI** - SDXL image generation
- **Firebase** - Backend infrastructure
- **Google AdMob** - Monetization platform
- **FreshWalls** - Inspiration for ad implementation patterns

---

## 📞 Support

- **Issues:** [GitHub Issues](https://github.com/Rahul-999-alpha/WallCraft/issues)
- **Discussions:** [GitHub Discussions](https://github.com/Rahul-999-alpha/WallCraft/discussions)
- **Email:** rahul.daswani@example.com

---

## 🗺️ Roadmap

### Phase 1: Foundation (✅ Complete)
- Core wallpaper browsing and search
- Multi-source integration (Pixabay, Wallhaven)
- AI generation with Stability AI
- Favorites and offline access
- AdMob integration (5 ad types)
- Theme system (Light/Dark/AMOLED)

### Phase 2: Enhancement (🚧 In Progress)
- [ ] Pexels, Unsplash, Pinterest, Freepik APIs
- [ ] Native ad cards in wallpaper grid
- [ ] Auto-refresh with WorkManager
- [ ] Hidden admin panel
- [ ] Wallpaper editor (blur, hue)
- [ ] Trending tab
- [ ] Google sign-in

### Phase 3: Advanced Features
- [ ] Social features (share, like, comment)
- [ ] Collections/boards
- [ ] Custom categories
- [ ] Wallpaper upload (UGC)
- [ ] Premium subscription tier
- [ ] Live wallpaper support

---

<div align="center">
  Made with ❤️ by Rahul Daswani

  ⭐ Star this repo if you find it helpful!
</div>
