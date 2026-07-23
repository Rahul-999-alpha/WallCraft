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

    // Ad-frequency knobs moved to Firebase Remote Config — see core/util/AdTuning.kt
    // (server-tunable, floor-clamped; in-code defaults apply until an override lands).

    const val SEARCH_DEBOUNCE_MS = 500L

    const val PREFS_DATASTORE_NAME = "clearwalls_prefs"

    // WorkManager
    const val REFRESH_WORK_NAME = "wallpaper_refresh"
    const val AUTO_WALLPAPER_WORK_NAME = "auto_wallpaper"
    const val DEFAULT_REFRESH_INTERVAL_HOURS = 11L

    // Notifications — daily, not every 4 hours: promotional pings 6x/day drive
    // uninstalls and channel mutes on a brand-new app.
    const val NOTIFICATION_WORK_NAME = "new_wallpaper_notification"
    const val NOTIFICATION_INTERVAL_HOURS = 24L
}
