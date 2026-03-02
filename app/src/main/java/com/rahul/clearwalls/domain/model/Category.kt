package com.rahul.clearwalls.domain.model

data class Category(
    val id: String,
    val name: String,
    val displayName: String,
    val thumbnailUrl: String? = null,
    val wallpaperCount: Int = 0,
    val isPinned: Boolean = false
)
