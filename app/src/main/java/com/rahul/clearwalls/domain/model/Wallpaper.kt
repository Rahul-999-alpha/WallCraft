package com.rahul.clearwalls.domain.model

data class Wallpaper(
    val id: String,
    val source: WallpaperSource,
    val title: String,
    val thumbnailUrl: String,
    val previewUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int,
    val dominantColor: String? = null,
    val tags: List<String> = emptyList(),
    val category: String? = null,
    val isFavorite: Boolean = false,
    val isAmoled: Boolean = false,
    val lowUrl: String? = null,
    val hdUrl: String? = null,
    val twoKUrl: String? = null,
    val fourKUrl: String? = null,
    val photographer: String? = null,
    val photographerUrl: String? = null,
    val sourceUrl: String? = null
)
