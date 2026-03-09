# ClearWalls - AI-Powered Wallpaper Application

<div align="center">
  <img src="Clear walls A1.png" alt="ClearWalls Logo" width="200"/>

  [![Version](https://img.shields.io/badge/version-1.0.5-blue.svg)](https://github.com/Rahul-999-alpha/WallCraft/releases)
  [![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
  [![Android](https://img.shields.io/badge/Android-26%2B-brightgreen.svg)](https://developer.android.com)
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org)
</div>

## Overview

ClearWalls is a modern Android wallpaper app that combines curated wallpapers from Pexels and Unsplash with AI-powered wallpaper generation via Stability AI. Built with Jetpack Compose and Material 3.

### Key Features

- **Multi-Source Wallpapers** - Browse wallpapers from Pexels and Unsplash
- **AI Generation** - Create custom wallpapers using Stability AI (SDXL)
- **Quality Options** - Download in multiple resolutions (480p, 1080p, 2K, 4K)
- **Favorites** - Save and organize wallpapers for offline access
- **Smart Search** - Debounced keyword search across all sources
- **Theme Modes** - Light, Dark, AMOLED (pure black), and System themes
- **Category Browse** - Explore curated categories (Nature, Abstract, Space, etc.)
- **Auto Wallpaper Changer** - Rotate wallpapers at set intervals via WorkManager
- **Set Wallpaper** - Apply to home screen, lock screen, or both
- **Native Ad Cards** - Non-intrusive ads integrated into the wallpaper grid

---

## Architecture

ClearWalls follows **Clean Architecture** with MVVM:

```
app/
├── core/
│   ├── common/          # Constants, sealed classes
│   ├── util/            # AdManager, Extensions
│   └── di/              # Hilt DI modules
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
├── worker/              # WorkManager jobs
└── presentation/
    ├── components/      # Reusable UI components (NativeAdCard, etc.)
    ├── navigation/      # Navigation setup
    ├── theme/           # Theme configuration
    └── [screens]/       # Feature screens with ViewModels
```

### Tech Stack

| Category | Libraries |
|----------|-----------|
| **Core** | Kotlin 1.9, Android SDK 26+ (target 35), Jetpack Compose (BOM 2024.12.01), Material 3 |
| **Architecture** | MVVM, Hilt DI, Coroutines & Flow, StateFlow |
| **Networking** | Retrofit 2.11, OkHttp 4.12 (with caching), Gson |
| **Data** | Room 2.6.1, DataStore Preferences, Paging 3 |
| **Images** | Coil 3.0.4 (Compose + OkHttp) |
| **Firebase** | Crashlytics, Analytics, Remote Config, Firestore, Storage |
| **Ads** | Google AdMob (Banner, Interstitial, Rewarded, Native, App Open) |
| **Background** | WorkManager 2.10.0 |

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35

### Setup

1. **Clone the repository:**
   ```bash
   git clone git@github.com-rahul:Rahul-999-alpha/WallCraft.git
   cd WallCraft
   ```

2. **Create `local.properties`** (copy from template):
   ```bash
   cp local.properties.template local.properties
   ```
   Then fill in your real API keys. See the template for details.

   **Required keys:**
   - `PEXELS_API_KEY` - from [pexels.com/api](https://www.pexels.com/api/)
   - `UNSPLASH_ACCESS_KEY` - from [unsplash.com/developers](https://unsplash.com/developers)
   - `ADMOB_APP_ID` + 5 ad unit IDs - from [AdMob console](https://admob.google.com)

   **Optional:**
   - `STABILITY_AI_API_KEY` - from [platform.stability.ai](https://platform.stability.ai/) (powers AI Create tab; shows error message if missing)

3. **Add `google-services.json`:**
   - Download from Firebase Console
   - Place in `app/` directory

4. **Release keystore** (for release builds):
   ```bash
   keytool -genkeypair -v -keystore keystore/clearwalls.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias clearwalls
   ```
   Then add to `local.properties`:
   ```properties
   KEY_STORE_PATH=../keystore/clearwalls.jks
   KEY_STORE_PASS=your_password
   KEY_ALIAS=clearwalls
   KEY_PASS=your_password
   ```

5. **Build:**
   ```bash
   ./gradlew assembleDebug      # Debug APK (~29 MB)
   ./gradlew assembleRelease    # Release APK (~7 MB, R8 minified + signed)
   ```

---

## Build Variants

### Debug
- **App ID:** `com.rahul.clearwalls.debug`
- Uses real AdMob IDs from `local.properties` (same as release)
- Debuggable, no obfuscation

### Release
- **App ID:** `com.rahul.clearwalls`
- `requireKey()` validates all AdMob IDs and content API keys at Gradle configuration time
- R8 minification + resource shrinking enabled
- ProGuard rules for AdMob, Retrofit, Gson, Coil, Room
- Requires signing configuration in `local.properties`

---

## API Integrations

### Active Sources
| Source | Purpose | Key Required |
|--------|---------|-------------|
| **Pexels** | Curated + search wallpapers | Yes |
| **Unsplash** | Search wallpapers | Yes |
| **Stability AI** | AI wallpaper generation (SDXL) | Optional |

### Disabled Sources (code preserved, not wired)
Pixabay, Wallhaven, Pinterest, and Freepik source files exist in `data/paging/` and `data/remote/` but are disconnected from the DI graph. To re-enable: uncomment providers in `NetworkModule.kt`, add keys to `local.properties`, and wire into `MergedWallpaperPagingSource`.

### Firebase
- **Firestore** - Curated wallpaper metadata
- **Storage** - Hosted wallpaper images
- **Remote Config** - Feature flags
- **Crashlytics** - Crash reporting
- **Analytics** - Usage tracking

---

## Ad Integration

ClearWalls uses Google AdMob with 5 ad formats. Both debug and release builds use production ad IDs.

| Ad Type | Placement | Frequency |
|---------|-----------|-----------|
| **Banner** | Bottom of Home/Browse/Favorites | Persistent (adaptive width) |
| **Interstitial** | After wallpaper actions | Every 3rd download / 5th set, 1-min cooldown |
| **Rewarded** | AI Generate (credits depleted) | On-demand |
| **Native** | Wallpaper grid | Every 8 items (full-width card) |
| **App Open** | App resume from background | 4-hour cooldown, 5-min first-session grace |

### Ad System Architecture
- `AdManager.kt` - Singleton managing all ad lifecycle (load, show, preload)
- `ClearWallsApp.kt` - Initializes MobileAds SDK, preloads ads in init callback
- `AdBanner.kt` - Composable adaptive banner component
- `NativeAdCard.kt` - Composable native ad card for grid integration

### Monitoring
```bash
adb logcat | grep -E "AdManager|AdBanner|ClearWallsApp|NATIVE"
```

---

## Screens

1. **Onboarding** - First-time user introduction (3 pages)
2. **Home** - Categories, search, wallpaper grid with native ad cards
3. **Browse** - Category-filtered wallpaper grid
4. **Favorites** - Saved wallpapers (offline access)
5. **AI Generate** - Stability AI wallpaper generation with rewarded ads
6. **Wallpaper Detail** - Preview, zoom, download, set, share
7. **Settings** - Theme, image quality, data saver, auto wallpaper
8. **Admin Panel** - Hidden configuration (7 taps on logo + password)

---

## Performance

- **Image Cache:** 250 MB Coil disk cache
- **HTTP Cache:** 50 MB OkHttp cache
- **Paging:** 20 items per page with 5-item prefetch distance
- **Search:** 500ms debounce
- **APK Size:** ~7 MB release (R8 optimized) vs ~29 MB debug

---

## Design System

### Colors
Purple gradient mountains with warm sunset tones:
- **Primary:** `#7C3AED` (Violet 600)
- **Secondary:** `#A78BFA` (Violet 400)
- **Tertiary:** `#F59E0B` (Amber 500)
- **Dark Surface:** `#0F0D1A` (deep purple-black)
- **AMOLED:** Pure black (`#000000`)

### Typography
- **Font Family:** Inter (Google Fonts)

---

## Changelog

### v1.0.5-patch1 (2026-03-09)
- **Fixed:** Debug builds now use real AdMob IDs (was showing test ads to distributed users)
- **Removed:** Pixabay, Wallhaven, Pinterest, Freepik from active pipeline (no real API keys)
- **Rewritten:** AdManager with lifecycle-aware loading and proper error handling
- **Rewritten:** MergedWallpaperPagingSource for Pexels + Unsplash only (15 results each)
- **Fixed:** NativeAdCard log emoji replaced with ASCII prefixes (R8 truncation fix)
- **Fixed:** Stability AI key made optional for release builds
- **Added:** `local.properties.template` with setup instructions

### v1.0.5 (2026-03-03)
- **Fixed:** Ad SDK initialization race condition (ads loaded before SDK ready)
- **Fixed:** Missing `gma_ad_services_config.xml` for Android Privacy Sandbox
- **Fixed:** Banner ad size changed from fixed 320x50 to adaptive
- **Added:** Enhanced ad error diagnostics with domain/cause logging

### v1.0.4 (2026-03-03)
- **Fixed:** Ads stripped by R8/ProGuard in release builds (missing keep rules)
- **Fixed:** Placeholder API keys causing silent HTTP 401 failures
- **Added:** `isValidApiKey()` helper for placeholder detection
- **Added:** Comprehensive logging to all PagingSource classes

### v1.0.3
- **Fixed:** Interstitial ads never shown (were preloaded but never triggered)
- **Fixed:** Download and set wallpaper completely broken (null wallpaper data)
- **Added:** Native ad cards in wallpaper grid (every 8 items)
- **Added:** NativeAdCard with media, headline, body, CTA

### v1.0.2
- **Fixed:** Native ad loading used wrong unit ID (BANNER instead of NATIVE)
- **Added:** Comprehensive AdManager logging with emoji indicators
- **Fixed:** Dynamic version display in Settings (uses BuildConfig)

### v1.0.1
- Initial public release with Pixabay, Wallhaven, Stability AI
- AdMob integration (Banner, Interstitial, Rewarded, App Open)

---

## Roadmap

### Completed
- Core wallpaper browsing and search
- Multi-source integration (Pexels, Unsplash)
- AI generation with Stability AI
- Favorites and offline access
- Full AdMob integration (5 ad types including native grid cards)
- Theme system (Light/Dark/AMOLED/System)
- Auto wallpaper changer with WorkManager
- Admin panel

### Planned
- Additional API sources (Pixabay, Wallhaven, Freepik) when keys are obtained
- Wallpaper editor (blur, hue adjustment)
- Trending tab
- Google sign-in
- Collections/boards
- Live wallpaper support

---

## Contributing

1. Fork the repository
2. Create a feature branch
3. Follow Clean Architecture layers and MVVM pattern
4. Test both debug and release builds
5. Submit a pull request

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- **Pexels** - High-quality free photos
- **Unsplash** - Professional photography
- **Stability AI** - SDXL image generation
- **Firebase** - Backend infrastructure
- **Google AdMob** - Monetization platform

---

<div align="center">
  Made by Rahul Daswani
</div>
