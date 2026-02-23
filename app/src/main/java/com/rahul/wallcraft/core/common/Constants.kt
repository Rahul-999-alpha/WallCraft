package com.rahul.wallcraft.core.common

object Constants {
    const val PIXABAY_BASE_URL = "https://pixabay.com/"
    const val WALLHAVEN_BASE_URL = "https://wallhaven.cc/api/"
    const val STABILITY_AI_BASE_URL = "https://api.stability.ai/"

    const val DATABASE_NAME = "wallcraft_db"

    const val PAGING_PAGE_SIZE = 20
    const val PAGING_PREFETCH_DISTANCE = 5

    const val IMAGE_CACHE_SIZE = 250L * 1024 * 1024 // 250MB
    const val HTTP_CACHE_SIZE = 50L * 1024 * 1024 // 50MB

    const val AD_INTERSTITIAL_COOLDOWN_MS = 60_000L
    const val AD_FIRST_SESSION_GRACE_MS = 300_000L // 5 min
    const val AD_INLINE_INTERVAL = 12
    const val AD_INTERSTITIAL_SET_INTERVAL = 5
    const val AD_INTERSTITIAL_DOWNLOAD_INTERVAL = 3

    const val SEARCH_DEBOUNCE_MS = 500L

    const val PREFS_DATASTORE_NAME = "wallcraft_prefs"
}
