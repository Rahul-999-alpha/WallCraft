# ClearWalls - Ongoing Fixes & Testing Log

## Current Version: v1.0.7 (2026-03-10)
**Previous Release:** v1.0.6 (2026-03-09)
**Repository:** https://github.com/Rahul-999-alpha/WallCraft

---

## v1.0.7 Changes (2026-03-10)

### 20. Admin Password Hash Wrong - FIXED
**File:** `Constants.kt`

**Root Cause:** `ADMIN_PASSWORD_HASH` was `a0f3285b...` but the actual SHA-256 of "clearwalls2024" is `c9a2892b...`. The hash was incorrect from the start.

**Fix:** Replaced with the correct SHA-256 hash.

---

### 21. Ad Frequency Too Low - FIXED
**File:** `Constants.kt`

**Root Cause:** Ads appeared too infrequently. Grace period was 5 min, inline ads every 8 wallpapers, interstitial every 5 sets / 3 downloads.

**Fix:**
```kotlin
AD_FIRST_SESSION_GRACE_MS = 120_000L  // was 300_000L (5 min) → 2 min
AD_INLINE_INTERVAL = 6                // was 8 → every 6 wallpapers
AD_INTERSTITIAL_SET_INTERVAL = 3      // was 5 → every 3 set-wallpaper actions
AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 2 // was 3 → every 2 downloads
```

---

### 22. Puter.js WebView AI Unreliable - REPLACED
**Files:** `PollinationsAiService.kt` (new), `AiGenerationRepositoryImpl.kt`, `NetworkModule.kt`
**Deleted:** `PuterAiService.kt`, `assets/puter_ai.html`

**Root Cause:** Puter.js WebView bridge had page load timing issues and JS bridge failures. The WebView approach was inherently fragile.

**Fix:** Replaced with Pollinations.ai — a free, no-auth HTTP API. Simple OkHttp GET request to `https://image.pollinations.ai/prompt/{prompt}` returns PNG bytes directly. No WebView, no JavaScript bridge, no HTML assets.

---

### 23. 4K Download Bypasses Ad Gate - FIXED
**Files:** `WallpaperDetailViewModel.kt`, `QualityPickerSheet.kt`, `WallpaperDetailScreen.kt`

**Root Cause:** `QualityPickerSheet` showed a lock icon on 4K but clicking it directly called `onQualitySelected(quality)` without requiring a rewarded ad. Anyone could download 4K for free.

**Fix:** Added `ShowRewardedForPremium` event and `onPremiumSelected` callback. Premium qualities now trigger a rewarded ad via `adManager.showRewarded()`. Download only starts after ad completion (`onRewarded` callback).

---

### 24. Editor's Picks Don't Refresh on App Open - FIXED
**Files:** `HomeViewModel.kt`, `HomeScreen.kt`

**Root Cause:** `wallpapers` and `editorPicks` flows were created once at ViewModel init and `cachedIn(viewModelScope)`. They never re-fetched unless the ViewModel was recreated (which doesn't happen on bottom-nav-retained screens).

**Fix:** Added `_refreshTrigger` MutableStateFlow with `flatMapLatest` to both flows. Added `LifecycleResumeEffect` in HomeScreen that calls `viewModel.refresh()` every time the composable resumes (app foregrounded, tab revisited).

---

### 25. No Push Notifications - IMPLEMENTED
**Files:** `NewWallpaperNotificationWorker.kt` (new), `ic_notification.xml` (new), `ClearWallsApp.kt`, `Constants.kt`

**Implementation:** `PeriodicWorkRequestBuilder` with 4-hour interval, 30-min flex, and 4-hour initial delay. Worker picks a random message from 4 options and posts a notification that opens the app. Notification channel: "New Wallpapers" (IMPORTANCE_DEFAULT).

---

## v1.0.6 Changes (2026-03-09)

### v1.0.6 Summary
- Added Puter.js WebView bridge for AI generation (replaced Stability AI)
- Fixed Browse category title display
- Fixed native ad validator warnings
- Overhauled ad system with interstitial gating, rewarded ads, app open ads

---

## v1.0.5-patch1 Changes (2026-03-09)

### 14. Debug Builds Using Real AdMob IDs - FIXED
**Files:** `app/build.gradle.kts`

**Root Cause:** Debug buildType had hardcoded Google test AdMob IDs (`ca-app-pub-3940256099942544/...`). Since debug-signed APKs are distributed to real users, they were always seeing test ads instead of production ads.

**Fix:** Debug block now reads real AdMob IDs from `local.properties` via `localProperties.getProperty()`, identical to how release builds work.

```kotlin
// BEFORE (test IDs - caused test ads for real users)
buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")

// AFTER (real IDs from local.properties)
buildConfigField("String", "ADMOB_BANNER_ID", "\"${localProperties.getProperty("ADMOB_BANNER_ID", "")}\"")
```

---

### 15. Disabled API Sources Removed from Pipeline - CLEANED
**Files:** `NetworkModule.kt`, `MergedWallpaperPagingSource.kt`, `WallpaperRepositoryImpl.kt`, `WallpaperRefreshWorker.kt`, `Constants.kt`, `build.gradle.kts`

**Root Cause:** Pixabay, Wallhaven, Pinterest, and Freepik had no real API keys but were wired into the app pipeline. Placeholder keys caused HTTP 401 errors that were silently swallowed, and the DI graph carried unnecessary complexity.

**Fix (comment-out strategy):**
- `NetworkModule.kt` - 4 DI providers commented out (Pixabay, Wallhaven, Pinterest, Freepik)
- `MergedWallpaperPagingSource.kt` - Rewritten for Pexels + Unsplash only (15 results each, interleaved)
- `WallpaperRepositoryImpl.kt` - Constructor reduced from 5 APIs to 2. Removed source-specific branches
- `WallpaperRefreshWorker.kt` - Simplified to Pexels curated fetch only
- `Constants.kt` - 4 BASE_URLs commented out
- `build.gradle.kts` - Empty BuildConfig stubs kept in defaultConfig so source files compile. `requireKey()` removed for disabled APIs in release block

**Re-enabling:** Uncomment providers in NetworkModule, add real keys to local.properties, wire into MergedWallpaperPagingSource constructor.

---

### 16. AdManager Rewritten - FIXED
**File:** `AdManager.kt`

Full rewrite with lifecycle-aware ad loading, proper null safety, and structured error handling. Previous version had stale reference bugs and missing preload-after-show logic.

---

### 17. NativeAdCard Emoji in Logs - FIXED
**File:** `NativeAdCard.kt`

R8 was truncating emoji characters in log strings. Replaced with ASCII prefixes:
- `[NATIVE] Loaded:` (was checkmark emoji)
- `[NATIVE] FAILED:` (was X emoji)
- `[NATIVE] Loading...` (was megaphone emoji)

---

### 18. Stability AI Key Made Optional - FIXED
**File:** `build.gradle.kts`

`requireKey("STABILITY_AI_API_KEY")` was in the release block but the key is empty (no real key yet). Removed the validation - the AI Create tab shows a user-facing error if the key is missing at runtime.

---

### 19. Release Keystore Configured - DONE
**File:** `keystore/clearwalls.jks` (gitignored)

Created RSA 2048-bit keystore with 10,000-day validity. Configured in `local.properties` via `KEY_STORE_PATH`, `KEY_STORE_PASS`, `KEY_ALIAS`, `KEY_PASS`.

---

## v1.0.5 Changes (2026-03-03)

### 10. Ad SDK Initialization Race Condition - FIXED
**File:** `ClearWallsApp.kt`

`MobileAds.initialize()` is async - ad preloading was called before SDK was ready. Fixed by moving preload calls into the init callback.

### 11. Missing gma_ad_services_config.xml - FIXED
**File:** `app/src/main/res/xml/gma_ad_services_config.xml`

AndroidManifest referenced this file but it never existed. Created with attribution and topics config for Android Privacy Sandbox.

### 12. Banner Ad Size: Fixed to Adaptive - FIXED
**File:** `AdBanner.kt`

Changed from fixed `AdSize.BANNER` (320x50) to `getCurrentOrientationAnchoredAdaptiveBannerAdSize()`.

### 13. Enhanced Ad Error Diagnostics - IMPLEMENTED
**Files:** `AdManager.kt`, `AdBanner.kt`

All error callbacks now log: error code, message, domain, and cause.

---

## v1.0.4 Changes (2026-03-03)

### 7. Ads Stripped by R8/ProGuard - FIXED
**File:** `proguard-rules.pro`

Missing keep rules for `com.google.android.gms.ads.**`. R8 stripped ad classes in release builds.

### 8. Placeholder API Keys Causing Silent Failures - FIXED
**Files:** All PagingSource files

Added `isValidApiKey()` to detect `your_*` / `*_here` patterns.

### 9. Comprehensive PagingSource Logging - IMPLEMENTED
All 6 PagingSource files log load counts, skipped sources, and errors.

---

## v1.0.3 Changes

### 4. Interstitial Ads Never Shown - FIXED
**Files:** `WallpaperDetailViewModel.kt`, `WallpaperDetailScreen.kt`

`shouldShowInterstitialOnSet()` and `shouldShowInterstitialOnDownload()` were defined but never called from UI.

### 5. Native Ads Integrated into Grid - IMPLEMENTED
**Files:** `WallpaperGrid.kt`, `HomeScreen.kt`, `NativeAdCard.kt`

NativeAdCard with media, headline, body, CTA. Shows every 8 items as full-width card.

### 6. Download & Set Wallpaper Broken - FIXED
**Files:** `WallpaperDetailViewModel.kt`, `WallpaperRepositoryImpl.kt`, `NavGraph.kt`, DAOs

`getWallpaperById()` was a null stub. Fixed with 2-tier loading: companion object cache + Room fallback.

---

## v1.0.2 Changes

### 1. Native Ad Wrong Unit ID - FIXED
Used `ADMOB_BANNER_ID` instead of `ADMOB_NATIVE_ID`.

### 2. Comprehensive AdManager Logging - IMPLEMENTED
TAG constant + emoji-based log indicators for all ad lifecycle events.

### 3. Hardcoded Version String - FIXED
Settings screen now uses `BuildConfig.VERSION_NAME`.

---

## Current API Key Status

| Source | Status | Working |
|--------|--------|---------|
| Pexels | Real key | Yes |
| Unsplash | Real key | Yes |
| Pollinations.ai | Free (no key) | Yes |
| Pixabay | Disconnected | N/A (no key) |
| Wallhaven | Disconnected | N/A (no key) |
| Pinterest | Disconnected | N/A (no key) |
| Freepik | Disconnected | N/A (no key) |

---

## Build Configuration

```kotlin
versionCode = 8
versionName = "1.0.7"
compileSdk = 35
minSdk = 26
targetSdk = 35
```

### AdMob IDs (from local.properties)
Both debug and release use the same real production IDs:
```
ADMOB_APP_ID=ca-app-pub-6988898499933953~6742917290
ADMOB_BANNER_ID=ca-app-pub-6988898499933953/856008435
ADMOB_INTERSTITIAL_ID=ca-app-pub-6988898499933953/9568987653
ADMOB_REWARDED_ID=ca-app-pub-6988898499933953/4620839342
ADMOB_NATIVE_ID=ca-app-pub-6988898499933953/9496568560
ADMOB_APP_OPEN_ID=ca-app-pub-6988898499933953/8255905989
```

---

## Testing Checklist

### Banner Ads
- [ ] Shows on Home screen bottom
- [ ] Shows on Browse screen bottom
- [ ] Shows on Favorites screen bottom
- [ ] Adaptive width fills screen

### Interstitial Ads
- [ ] Shows on 2nd download
- [ ] Shows on 3rd wallpaper set
- [ ] Respects 1-minute cooldown
- [ ] 2-minute first-session grace period

### Rewarded Ads
- [ ] Button appears when AI credits depleted
- [ ] Grants reward after completion
- [ ] 4K download triggers rewarded ad before download starts

### Native Ads
- [ ] Shows in wallpaper grid every 6 items
- [ ] Full-width card with media, headline, body, CTA

### App Open Ads
- [ ] Does NOT show during first 2 minutes
- [ ] Shows when app resumes from background

### AI Generation
- [ ] Type prompt → Pollinations.ai returns image (no WebView)
- [ ] Generated image saved and displayed

### Refresh on Resume
- [ ] Close app, reopen → Editor's Picks refresh
- [ ] Switch tabs and return → wallpapers refresh

### Notifications
- [ ] Notification worker scheduled (verify via `adb shell dumpsys jobscheduler`)
- [ ] Notification appears after 4 hours with random message

### Monitor
```bash
adb logcat | grep -E "AdManager|AdBanner|ClearWallsApp|NATIVE"
```

---

## Known Issues

**None currently.** The `applicationIdSuffix = ".debug"` was removed in v1.0.6 so both debug and release use `com.rahul.clearwalls`.

### WallpaperSource Enum
`WallpaperSource` enum still has `PIXABAY`, `WALLHAVEN` entries. These are kept because existing cached/favorite wallpapers in Room database may reference these source values.

---

**Last Updated:** 2026-03-10
**Git Branch:** master
