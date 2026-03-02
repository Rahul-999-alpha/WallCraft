package com.rahul.clearwalls.domain.model

data class AiGeneration(
    val id: String,
    val prompt: String,
    val enhancedPrompt: String,
    val imageUrl: String,
    val localPath: String? = null,
    val style: AiStyle = AiStyle.GENERAL,
    val isAmoled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AiStyle(val displayName: String, val promptSuffix: String) {
    GENERAL("General", "4K wallpaper, vibrant colors, highly detailed, sharp focus"),
    ABSTRACT("Abstract", "abstract art, 4K wallpaper, vibrant colors, geometric patterns"),
    NATURE("Nature", "nature photography, 4K wallpaper, stunning landscape, golden hour"),
    ANIME("Anime", "anime style, 4K wallpaper, studio quality, vibrant colors, detailed"),
    MINIMAL("Minimal", "minimalist design, 4K wallpaper, clean lines, simple composition"),
    CYBERPUNK("Cyberpunk", "cyberpunk aesthetic, 4K wallpaper, neon lights, futuristic cityscape"),
    FANTASY("Fantasy", "fantasy art, 4K wallpaper, epic, magical atmosphere, detailed"),
    SPACE("Space", "space art, 4K wallpaper, cosmic, nebula, stars, deep space")
}

data class AiQuota(
    val freeRemaining: Int,
    val bonusRemaining: Int,
    val maxFreePerDay: Int,
    val maxBonusAdsPerDay: Int,
    val bonusPerAd: Int,
    val adsWatchedToday: Int
) {
    val totalRemaining: Int get() = freeRemaining + bonusRemaining
    val canGenerate: Boolean get() = totalRemaining > 0
    val canWatchAd: Boolean get() = adsWatchedToday < maxBonusAdsPerDay
}
