package com.rahul.wallcraft.data.mapper

import com.rahul.wallcraft.data.local.entity.FavoriteEntity
import com.rahul.wallcraft.data.remote.dto.PixabayHit
import com.rahul.wallcraft.data.remote.dto.WallhavenWallpaper
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.model.WallpaperSource

fun PixabayHit.toWallpaper(): Wallpaper = Wallpaper(
    id = "${WallpaperSource.PIXABAY.prefix}_$id",
    source = WallpaperSource.PIXABAY,
    title = tags.split(",").firstOrNull()?.trim()?.replaceFirstChar { it.uppercase() } ?: "Wallpaper",
    thumbnailUrl = previewURL,
    previewUrl = webformatURL,
    fullUrl = largeImageURL,
    width = imageWidth,
    height = imageHeight,
    dominantColor = null,
    tags = tags.split(",").map { it.trim() },
    category = null,
    isAmoled = tags.lowercase().let { it.contains("dark") || it.contains("black") || it.contains("amoled") }
)

fun WallhavenWallpaper.toWallpaper(): Wallpaper = Wallpaper(
    id = "${WallpaperSource.WALLHAVEN.prefix}_$id",
    source = WallpaperSource.WALLHAVEN,
    title = "Wallpaper $id",
    thumbnailUrl = thumbs.small,
    previewUrl = thumbs.large,
    fullUrl = path,
    width = dimensionX,
    height = dimensionY,
    dominantColor = colors.firstOrNull(),
    tags = emptyList(),
    category = category,
    isAmoled = colors.any { color ->
        val hex = color.removePrefix("#").lowercase()
        hex.length == 6 && hex.chunked(2).all { it.toIntOrNull(16)?.let { v -> v < 30 } ?: false }
    }
)

fun FavoriteEntity.toWallpaper(): Wallpaper = Wallpaper(
    id = wallpaperId,
    source = WallpaperSource.valueOf(source),
    title = title,
    thumbnailUrl = thumbnailUrl,
    previewUrl = previewUrl,
    fullUrl = fullUrl,
    width = width,
    height = height,
    dominantColor = dominantColor,
    tags = tags.split(",").filter { it.isNotBlank() },
    category = category,
    isFavorite = true,
    isAmoled = isAmoled
)

fun Wallpaper.toFavoriteEntity(): FavoriteEntity = FavoriteEntity(
    wallpaperId = id,
    source = source.name,
    title = title,
    thumbnailUrl = thumbnailUrl,
    previewUrl = previewUrl,
    fullUrl = fullUrl,
    width = width,
    height = height,
    dominantColor = dominantColor,
    tags = tags.joinToString(","),
    category = category,
    isAmoled = isAmoled,
    addedAt = System.currentTimeMillis()
)
