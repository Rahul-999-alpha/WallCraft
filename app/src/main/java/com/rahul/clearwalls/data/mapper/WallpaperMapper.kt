package com.rahul.clearwalls.data.mapper

import com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity
import com.rahul.clearwalls.data.local.entity.FavoriteEntity
import com.rahul.clearwalls.data.remote.dto.FreepikResource
import com.rahul.clearwalls.data.remote.dto.PexelsPhoto
import com.rahul.clearwalls.data.remote.dto.PinterestPin
import com.rahul.clearwalls.data.remote.dto.PixabayHit
import com.rahul.clearwalls.data.remote.dto.UnsplashPhoto
import com.rahul.clearwalls.data.remote.dto.WallhavenWallpaper
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource

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
    isAmoled = tags.lowercase().let { it.contains("dark") || it.contains("black") || it.contains("amoled") },
    lowUrl = previewURL,
    hdUrl = webformatURL,
    twoKUrl = largeImageURL,
    fourKUrl = imageURL ?: largeImageURL,
    photographer = user,
    photographerUrl = null,
    sourceUrl = pageURL
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
    },
    lowUrl = thumbs.small,
    hdUrl = thumbs.large,
    twoKUrl = path,
    fourKUrl = path,
    sourceUrl = url
)

fun PexelsPhoto.toWallpaper(): Wallpaper = Wallpaper(
    id = "${WallpaperSource.PEXELS.prefix}_$id",
    source = WallpaperSource.PEXELS,
    title = alt?.replaceFirstChar { it.uppercase() } ?: "Pexels Photo",
    thumbnailUrl = src.tiny,
    previewUrl = src.large,
    fullUrl = src.original,
    width = width,
    height = height,
    dominantColor = avgColor,
    tags = emptyList(),
    category = null,
    isAmoled = false,
    lowUrl = src.small,
    hdUrl = src.large,
    twoKUrl = src.large2x,
    fourKUrl = src.original,
    photographer = photographer,
    photographerUrl = photographerUrl,
    sourceUrl = url
)

fun UnsplashPhoto.toWallpaper(): Wallpaper = Wallpaper(
    id = "${WallpaperSource.UNSPLASH.prefix}_$id",
    source = WallpaperSource.UNSPLASH,
    title = description ?: altDescription?.replaceFirstChar { it.uppercase() } ?: "Unsplash Photo",
    thumbnailUrl = urls.thumb,
    previewUrl = urls.regular,
    fullUrl = urls.full,
    width = width,
    height = height,
    dominantColor = color,
    tags = emptyList(),
    category = null,
    isAmoled = false,
    lowUrl = urls.small,
    hdUrl = urls.regular,
    twoKUrl = urls.full,
    fourKUrl = urls.raw,
    photographer = user.name,
    photographerUrl = user.portfolioUrl,
    sourceUrl = links.html
)

fun PinterestPin.toWallpaper(): Wallpaper? {
    val imageUrl = media?.images?.get("originals")?.url
        ?: media?.images?.values?.firstOrNull()?.url
        ?: return null
    val img = media?.images?.get("originals") ?: media?.images?.values?.firstOrNull()

    return Wallpaper(
        id = "${WallpaperSource.PINTEREST.prefix}_$id",
        source = WallpaperSource.PINTEREST,
        title = title ?: "Pinterest Pin",
        thumbnailUrl = media?.images?.get("150x150")?.url ?: imageUrl,
        previewUrl = media?.images?.get("600x")?.url ?: imageUrl,
        fullUrl = imageUrl,
        width = img?.width ?: 1080,
        height = img?.height ?: 1920,
        dominantColor = dominantColor,
        tags = emptyList(),
        category = null,
        isAmoled = false,
        lowUrl = media?.images?.get("150x150")?.url,
        hdUrl = media?.images?.get("600x")?.url,
        twoKUrl = imageUrl,
        fourKUrl = imageUrl,
        sourceUrl = link
    )
}

fun FreepikResource.toWallpaper(): Wallpaper? {
    val imgUrl = image?.source?.url ?: return null

    return Wallpaper(
        id = "${WallpaperSource.FREEPIK.prefix}_$id",
        source = WallpaperSource.FREEPIK,
        title = title ?: "Freepik Resource",
        thumbnailUrl = imgUrl,
        previewUrl = imgUrl,
        fullUrl = imgUrl,
        width = 1080,
        height = 1920,
        dominantColor = null,
        tags = emptyList(),
        category = null,
        isAmoled = false,
        lowUrl = imgUrl,
        hdUrl = imgUrl,
        twoKUrl = imgUrl,
        fourKUrl = imgUrl,
        sourceUrl = url
    )
}

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

fun CachedWallpaperEntity.toWallpaper(): Wallpaper = Wallpaper(
    id = id,
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
    isAmoled = isAmoled,
    lowUrl = lowUrl,
    hdUrl = hdUrl,
    twoKUrl = twoKUrl,
    fourKUrl = fourKUrl,
    photographer = photographer,
    photographerUrl = photographerUrl,
    sourceUrl = sourceUrl
)
