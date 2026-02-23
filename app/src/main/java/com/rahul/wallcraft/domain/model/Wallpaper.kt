package com.rahul.wallcraft.domain.model

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
    val isAmoled: Boolean = false
)
