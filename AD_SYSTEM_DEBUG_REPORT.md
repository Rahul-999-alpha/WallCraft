# ClearWalls Ad System - Debug Report

## Current State (v1.0.5-patch1)

All 5 ad types are implemented, integrated into UI, and using production AdMob IDs in both debug and release builds.

### Ad Types Status

| Ad Type | Component | Status | Notes |
|---------|-----------|--------|-------|
| **Banner** | `AdBanner.kt` | Implemented | Adaptive width, bottom of Home/Browse/Favorites |
| **Interstitial** | `AdManager.kt` | Implemented | Every 3rd download / 5th set, 1-min cooldown |
| **Rewarded** | `AdManager.kt` | Implemented | AI Generate screen when credits depleted |
| **Native** | `NativeAdCard.kt` | Implemented | Every 8 items in wallpaper grid, full-width |
| **App Open** | `AdManager.kt` | Implemented | On resume, 4-hr cooldown, 5-min grace |

---

## Architecture

### Ad Initialization Flow
```
ClearWallsApp.onCreate()
  -> MobileAds.initialize(this) { callback ->
       -> adManager.preloadInterstitial()
       -> adManager.preloadRewarded()
       -> adManager.loadAppOpenAd()
     }
```

Ads are only preloaded AFTER the SDK init callback fires (fixed in v1.0.5).

### Key Files
| File | Role |
|------|------|
| `ClearWallsApp.kt` | SDK init + ad preloading in callback |
| `AdManager.kt` | Singleton: load, show, preload lifecycle for all ad types |
| `AdBanner.kt` | Composable adaptive banner |
| `NativeAdCard.kt` | Composable native ad card for grid |
| `WallpaperDetailScreen.kt` | Triggers interstitial after download/set |
| `WallpaperDetailViewModel.kt` | Checks shouldShowInterstitial counters |
| `AiGenerateScreen.kt` | Triggers rewarded ad when credits depleted |
| `MainActivity.kt` | App open ad on resume |

### Ad Unit IDs

Both debug and release builds use the same production IDs from `local.properties`:

```
ADMOB_APP_ID            = ca-app-pub-6988898499933953~6742917290
ADMOB_BANNER_ID         = ca-app-pub-6988898499933953/856008435
ADMOB_INTERSTITIAL_ID   = ca-app-pub-6988898499933953/9568987653
ADMOB_REWARDED_ID       = ca-app-pub-6988898499933953/4620839342
ADMOB_NATIVE_ID         = ca-app-pub-6988898499933953/9496568560
ADMOB_APP_OPEN_ID       = ca-app-pub-6988898499933953/8255905989
```

Previously, debug builds used Google test IDs (`ca-app-pub-3940256099942544/...`), which caused test ads to appear for real users receiving debug-signed APKs. Fixed in v1.0.5-patch1.

---

## Frequency & Cooldown Configuration

From `Constants.kt`:

```kotlin
AD_INTERSTITIAL_SET_INTERVAL = 5         // Every 5 wallpaper sets
AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 3    // Every 3 downloads
AD_INTERSTITIAL_COOLDOWN_MS = 60_000L    // 1 minute between interstitials
AD_FIRST_SESSION_GRACE_MS = 300_000L     // 5 minutes grace on first launch
AD_INLINE_INTERVAL = 8                   // Native ad every 8 wallpapers in grid
```

App open ad: 4-hour cooldown (hardcoded in AdManager).

---

## Bug History

### Fixed Bugs

| Bug | Version | Description |
|-----|---------|-------------|
| Wrong native ad unit ID | v1.0.2 | Used BANNER_ID instead of NATIVE_ID |
| Interstitial never shown | v1.0.3 | shouldShow methods defined but never called |
| Native ads not in UI | v1.0.3 | NativeAdCard existed but was never used |
| Download/set broken | v1.0.3 | Wallpaper data never reached ViewModel |
| Ads stripped by R8 | v1.0.4 | Missing ProGuard keep rules |
| Placeholder keys | v1.0.4 | `your_*` patterns passed to APIs as real keys |
| SDK init race | v1.0.5 | Ads preloaded before SDK init callback |
| Missing ad services XML | v1.0.5 | gma_ad_services_config.xml never existed |
| Fixed banner size | v1.0.5 | Changed to adaptive width |
| Test ads in debug | v1.0.5-p1 | Debug used Google test IDs, users got test ads |
| Log emoji truncation | v1.0.5-p1 | R8 truncated emoji in NativeAdCard logs |

### Known Issues

**applicationIdSuffix:** Debug package is `com.rahul.clearwalls.debug`. If AdMob console only has `com.rahul.clearwalls` registered, debug builds may get no-fill or app-not-authorized errors.

---

## ProGuard / R8 Rules

`proguard-rules.pro` includes keep rules for:
- `com.google.android.gms.ads.**`
- `com.google.ads.**`
- `com.google.android.gms.common.**`
- `com.google.android.gms.internal.**`

---

## Debug Commands

```bash
# Monitor all ad activity
adb logcat | grep -E "AdManager|AdBanner|ClearWallsApp|NATIVE"

# Monitor network requests to AdMob
adb logcat | grep -E "googleads|admob"

# Check SDK initialization
adb logcat | grep "Ad adapter"
```

### Log Prefixes
- `[NATIVE] Loading...` - Native ad load started
- `[NATIVE] Loaded:` - Native ad loaded successfully
- `[NATIVE] FAILED:` - Native ad failed to load

---

## Testing Plan

### Pre-Test
1. Build release APK: `./gradlew assembleRelease`
2. Install on real device (not emulator)
3. Connect logcat: `adb logcat | grep -E "AdManager|AdBanner|ClearWallsApp|NATIVE"`

### Test Matrix

| Test | Action | Expected |
|------|--------|----------|
| Banner | Open Home/Browse/Favorites | Banner at bottom within 5-10s |
| Interstitial (download) | Download 3 wallpapers | Full-screen ad on 3rd |
| Interstitial (set) | Set 5 wallpapers | Full-screen ad on 5th |
| Interstitial (cooldown) | Trigger twice in < 1 min | Second should be suppressed |
| Rewarded | Use all AI credits, tap button | Full-screen rewarded ad |
| Native | Scroll wallpaper grid | Full-width ad card every 8 items |
| App Open | Background app > 5 min, resume | Full-screen ad on resume |
| Grace period | Fresh install, background quickly | No app open ad for 5 min |

---

**Last Updated:** 2026-03-09
