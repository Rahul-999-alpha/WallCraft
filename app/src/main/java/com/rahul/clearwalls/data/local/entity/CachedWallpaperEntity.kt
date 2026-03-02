package com.rahul.clearwalls.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_wallpapers")
data class CachedWallpaperEntity(
    @PrimaryKey val id: String,
    val source: String,
    val title: String,
    val thumbnailUrl: String,
    val previewUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int,
    val dominantColor: String?,
    val tags: String,
    val category: String?,
    val isAmoled: Boolean,
    val lowUrl: String?,
    val hdUrl: String?,
    val twoKUrl: String?,
    val fourKUrl: String?,
    val photographer: String?,
    val photographerUrl: String?,
    val sourceUrl: String?,
    val cachedAt: Long
)
