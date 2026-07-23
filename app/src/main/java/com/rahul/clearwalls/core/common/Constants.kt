package com.rahul.clearwalls.core.common

object Constants {
    const val PEXELS_BASE_URL = "https://api.pexels.com/"
    const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
    const val STABILITY_AI_BASE_URL = "https://api.stability.ai/"

    // DISABLED — no API keys. Uncomment when keys are obtained.
    // const val PIXABAY_BASE_URL = "https://pixabay.com/"
    // const val WALLHAVEN_BASE_URL = "https://wallhaven.cc/api/"
    // const val PINTEREST_BASE_URL = "https://api.pinterest.com/"
    // const val FREEPIK_BASE_URL = "https://api.freepik.com/"

    const val DATABASE_NAME = "clearwalls_db"
    const val DATABASE_VERSION = 2

    const val PAGING_PAGE_SIZE = 20
    const val PAGING_PREFETCH_DISTANCE = 5

    const val IMAGE_CACHE_SIZE = 250L * 1024 * 1024 // 250MB
    const val HTTP_CACHE_SIZE = 50L * 1024 * 1024 // 50MB

    // Ad load retuned for the Play release (v1.0.8). The v1.0.7 values (grace 2 min,
    // native every 6 tiles, interstitial every 2nd download with 60s cooldown) were
    // extraction tuning for an app with an existing loyal base — on a new listing they
    // read as adware and kill retention/ratings before any revenue accrues. Loosen
    // first, tighten later from real retention data.
    const val AD_INTERSTITIAL_COOLDOWN_MS = 180_000L      // 3 min between interstitials
    const val AD_FIRST_SESSION_GRACE_MS = 600_000L        // 10 min ad-free first session
    const val AD_INLINE_INTERVAL = 10                     // native card every 10 grid items
    const val AD_INTERSTITIAL_SET_INTERVAL = 4            // every 4th set-wallpaper action
    const val AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 4       // every 4th download

    const val SEARCH_DEBOUNCE_MS = 500L

    const val PREFS_DATASTORE_NAME = "clearwalls_prefs"

    // Admin panel — DEBUG builds only (the reveal gesture and route are gated on BuildConfig.DEBUG).
    // Rotated 2026-07-23: hash of a random password whose plaintext is only in the developer's
    // gitignored local.properties (never committed). It only unlocks a local, non-authoritative
    // DataStore config — not any server-side entitlement — so real premium access must be
    // enforced by Play Billing, not this flag.
    const val ADMIN_PASSWORD_HASH = "285a849897c424eeb73e693c420ac29a98fee64b2abbf71845f9b4edf2e22108"
    const val ADMIN_TAP_COUNT = 7

    // WorkManager
    const val REFRESH_WORK_NAME = "wallpaper_refresh"
    const val AUTO_WALLPAPER_WORK_NAME = "auto_wallpaper"
    const val DEFAULT_REFRESH_INTERVAL_HOURS = 11L

    // Notifications — daily, not every 4 hours: promotional pings 6x/day drive
    // uninstalls and channel mutes on a brand-new app.
    const val NOTIFICATION_WORK_NAME = "new_wallpaper_notification"
    const val NOTIFICATION_INTERVAL_HOURS = 24L
}
