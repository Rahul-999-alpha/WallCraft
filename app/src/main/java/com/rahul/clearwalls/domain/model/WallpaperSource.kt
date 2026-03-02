package com.rahul.clearwalls.domain.model

enum class WallpaperSource(val prefix: String, val displayName: String, val requiresAttribution: Boolean) {
    PIXABAY("px", "Pixabay", true),
    WALLHAVEN("wh", "Wallhaven", false),
    PEXELS("pe", "Pexels", true),
    UNSPLASH("un", "Unsplash", true),
    PINTEREST("pi", "Pinterest", false),
    FREEPIK("fp", "Freepik", true),
    AI_GENERATED("ai", "AI Generated", false),
    CURATED_FIREBASE("fb", "Curated", false)
}
