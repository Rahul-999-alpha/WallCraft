package com.rahul.clearwalls.core.common

object Constants {
    const val PIXABAY_BASE_URL = "https://pixabay.com/"
    const val WALLHAVEN_BASE_URL = "https://wallhaven.cc/api/"
    const val STABILITY_AI_BASE_URL = "https://api.stability.ai/"
    const val PEXELS_BASE_URL = "https://api.pexels.com/"
    const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
    const val PINTEREST_BASE_URL = "https://api.pinterest.com/"
    const val FREEPIK_BASE_URL = "https://api.freepik.com/"

    const val DATABASE_NAME = "clearwalls_db"
    const val DATABASE_VERSION = 2

    const val PAGING_PAGE_SIZE = 20
    const val PAGING_PREFETCH_DISTANCE = 5

    const val IMAGE_CACHE_SIZE = 250L * 1024 * 1024 // 250MB
    const val HTTP_CACHE_SIZE = 50L * 1024 * 1024 // 50MB

    const val AD_INTERSTITIAL_COOLDOWN_MS = 60_000L
    const val AD_FIRST_SESSION_GRACE_MS = 300_000L // 5 min
    const val AD_INLINE_INTERVAL = 8
    const val AD_INTERSTITIAL_SET_INTERVAL = 5
    const val AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 3

    const val SEARCH_DEBOUNCE_MS = 500L

    const val PREFS_DATASTORE_NAME = "clearwalls_prefs"

    // Admin panel
    const val ADMIN_PASSWORD_HASH = "a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15" // SHA-256 of "clearwalls2024"
    const val ADMIN_TAP_COUNT = 7

    // WorkManager
    const val REFRESH_WORK_NAME = "wallpaper_refresh"
    const val AUTO_WALLPAPER_WORK_NAME = "auto_wallpaper"
    const val DEFAULT_REFRESH_INTERVAL_HOURS = 11L
}
