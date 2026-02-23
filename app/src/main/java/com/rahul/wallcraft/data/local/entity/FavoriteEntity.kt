package com.rahul.wallcraft.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val wallpaperId: String,
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
    val addedAt: Long
)
