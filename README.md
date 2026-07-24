# ClearWalls - AI-Powered Wallpaper Application

<div align="center">
  <img src="Clear walls A1.png" alt="ClearWalls Logo" width="200"/>

  [![Version](https://img.shields.io/badge/version-1.0.8-blue.svg)](https://github.com/Rahul-999-alpha/clearwalls/releases)
  [![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
  [![Android](https://img.shields.io/badge/Android-26%2B-brightgreen.svg)](https://developer.android.com)
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org)
</div>

## Overview

ClearWalls is a modern Android wallpaper app serving an owned, curated catalog (AI-generated, hosted on Firebase) plus on-device AI wallpaper generation via Pollinations.ai. Built with Jetpack Compose and Material 3.

**Content model (v1.0.8):** Pexels/Unsplash were removed from the pipeline — both providers' API terms prohibit wallpaper apps. The catalog is generated and owned by us; see `tools/seed_wallpapers/`. **Publishing:** see `PUBLISHING.md` for the full Play Store runbook.

### Key Features

- **Owned Curated Catalog** - AI-generated wallpapers, hand-curated, served from Firebase (Firestore + Storage)
- **AI Generation** - Create custom wallpapers using Pollinations.ai (free, no API key; safe-mode + prompt moderation + in-app reporting)
- **Quality Options** - Download in multiple resolutions (480p, 1080p, 2K, 4K with rewarded ad gate)
- **Push Notifications** - Daily reminder about new wallpapers (permission-gated)
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
| **Core** | Kotlin 2.1, Android SDK 26+ (target 36, AGP 8.9.3), Jetpack Compose (BOM 2024.12.01), Material 3 |
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
   git clone git@github.com-rahul:Rahul-999-alpha/clearwalls.git
   cd clearwalls
   ```

2. **Create `local.properties`** (copy from template):
   ```bash
   cp local.properties.template local.properties
   ```
   Then fill in your real values. See the template for details.

   **Required for release builds:**
   - `ADMOB_APP_ID` + 5 ad unit IDs - from [AdMob console](https://admob.google.com)
   - `PRIVACY_POLICY_URL` - your hosted copy of `docs/privacy-policy.html`
   - Keystore path/passwords

   **Leave blank:** `PEXELS_API_KEY`, `UNSPLASH_ACCESS_KEY`, `STABILITY_AI_API_KEY`
   (sources disconnected — their API terms prohibit wallpaper apps).

   **Note:** AI generation uses Pollinations.ai (free, no API key required).
   Catalog content is seeded to your Firebase project via `tools/seed_wallpapers/`.

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
- **App ID:** `com.clearwalls`
- Uses real AdMob IDs from `local.properties` (same as release)
- Debuggable, no obfuscation

### Release
- **App ID:** `com.clearwalls`
- `requireKey()` validates all AdMob IDs and content API keys at Gradle configuration time
- R8 minification + resource shrinking enabled
- ProGuard rules for AdMob, Retrofit, Gson, Coil, Room
- Requires signing configuration in `local.properties`

---

## API Integrations

### Active Sources
| Source | Purpose | Key Required |
|--------|---------|-------------|
| **Firebase (owned)** | Curated catalog, editor picks, categories, search | No (google-services.json) |
| **Pollinations.ai** | AI wallpaper generation (free HTTP API, safe mode) | No |

### Disconnected Sources (code preserved, not wired)
Pexels and Unsplash were disconnected in v1.0.8 — **both providers' API terms
prohibit wallpaper apps** (Pexels revokes keys; Unsplash rejects production
approval). Pixabay, Wallhaven, Pinterest, and Freepik remain disconnected as
before. Do not re-enable any of them for a store build.

### Firebase
- **Firestore** - Curated wallpaper metadata
- **Storage** - Hosted wallpaper images
- **Remote Config** - Feature flags
- **Crashlytics** - Crash reporting
- **Analytics** - Usage tracking

---

## Ad Integration

ClearWalls uses Google AdMob with 5 ad formats. Debug builds use Google's public
test IDs; release builds read production IDs from `local.properties`. UMP consent
is gathered before any ad request (see `ConsentManager`); banner/native components
render nothing until `AdManager.adsEnabled` is true.

All frequencies are server-tunable without an app update via Firebase Remote
Config (`core/util/AdTuning.kt` — parameter names, defaults, and floor clamps).

| Ad Type | Placement | Launch default |
|---------|-----------|----------------|
| **Banner** | Bottom of Home/Browse/Favorites | Persistent (adaptive width) |
| **Interstitial** | After wallpaper actions | Every 4th download / 4th set, 3-min cooldown |
| **Rewarded** | AI Generate (credits) + 4K download | On-demand |
| **Native** | Wallpaper grid | Every 8 items (full-width card) |
| **App Open** | App resume from background | 4-hour cooldown, 10-min first-session grace |

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
5. **AI Generate** - Pollinations.ai wallpaper generation with rewarded ads
6. **Wallpaper Detail** - Preview, zoom, download, set, share
7. **Settings** - Theme, image quality, data saver, auto wallpaper

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

### v1.0.9 (2026-07-24)
- **Target API 36** (Android 16) + AGP 8.7.3 → 8.9.3 — Play's 31 Aug 2026
  requirement for app updates; versionCode 10
- **Native debug symbols** config (`debugSymbolLevel=SYMBOL_TABLE`; today's only
  native libs are pre-stripped AndroidX, so the Play warning is cosmetic)

### v1.0.8 (2026-07-22/23) — Play Store publish prep
- **Content pivot:** catalog now served from owned Firebase collections
  (`CuratedFirestorePagingSource`); Pexels/Unsplash disconnected (their API terms
  prohibit wallpaper apps). Seeder tool added at `tools/seed_wallpapers/`.
- **Consent:** Google UMP (certified CMP) gathers consent before any ad request;
  banners/native gated on `AdManager.adsEnabled`; "Ad privacy options" in Settings.
- **AI safety (Play AI-GC policy):** Pollinations `safe=true`, deterministic
  prompt blocklist (`PromptModeration` + unit tests), in-app Report on AI results
  and wallpaper detail (writes to Firestore `reports`).
- **Permissions:** removed unused `READ_MEDIA_IMAGES` (Play photo-permissions
  policy); notification worker respects denied POST_NOTIFICATIONS.
- **Ad load retuned for retention:** 10-min first-session grace, native every
  10 tiles, interstitial every 4th action with 3-min cooldown; notifications
  daily instead of every 4 hours.
- **Privacy:** `docs/privacy-policy.html` + required `PRIVACY_POLICY_URL`
  (release build fails without it); privacy policy link in Settings.
- **Docs:** `PUBLISHING.md` Play Console runbook.
- **Removed:** admin panel (inert config, hidden-feature review risk); ad
  frequencies are now server-tunable via Firebase Remote Config (`AdTuning`,
  floor-clamped) — the operational control the panel pretended to be.

### v1.0.7 (2026-03-10)
- **Fixed:** Admin password hash (wrong SHA-256 value)
- **Changed:** Ad frequency increased — grace period 5min→2min, inline every 8→6, interstitial every 5/3→3/2
- **Replaced:** Puter.js WebView AI with Pollinations.ai HTTP API (more reliable, no WebView needed)
- **Fixed:** 4K download bypassed rewarded ad gate — now requires watching ad for premium qualities
- **Added:** Refresh-on-resume for Editor's Picks and wallpapers (LifecycleResumeEffect)
- **Added:** Push notifications every 4 hours for new wallpapers (WorkManager)

### v1.0.6 (2026-03-09)
- **Added:** Puter.js WebView bridge for AI generation (replaced by Pollinations.ai in v1.0.7)
- **Fixed:** Browse category title, native ad validator warnings
- **Overhauled:** Ad system with interstitial gating, rewarded ads, app open ads

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

### Planned
- API 36 / AGP 8.9+ toolchain bump (required before 31 Aug 2026 — see PUBLISHING.md §9)
- Weekly catalog refreshes via the seeder (retention lever)
- "Remove ads" one-time IAP via Play Billing (after retention data)
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
- **Pollinations.ai** - Free AI image generation
- **Firebase** - Backend infrastructure
- **Google AdMob** - Monetization platform

---

<div align="center">
  Made by Rahul Daswani
</div>
