package com.rahul.clearwalls.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WallhavenResponse(
    val data: List<WallhavenWallpaper>,
    val meta: WallhavenMeta
)

data class WallhavenWallpaper(
    val id: String,
    val url: String,
    val resolution: String,
    @SerializedName("dimension_x") val dimensionX: Int,
    @SerializedName("dimension_y") val dimensionY: Int,
    val colors: List<String>,
    val path: String,
    val thumbs: WallhavenThumbs,
    val category: String,
    val purity: String,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("created_at") val createdAt: String
)

data class WallhavenThumbs(
    val large: String,
    val original: String,
    val small: String
)

data class WallhavenMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("per_page") val perPage: Int,
    val total: Int
)
