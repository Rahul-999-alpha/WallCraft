package com.rahul.wallcraft.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PixabayResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixabayHit>
)

data class PixabayHit(
    val id: Long,
    val pageURL: String,
    val type: String,
    val tags: String,
    @SerializedName("previewURL") val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageURL: String,
    val fullHDURL: String?,
    val imageURL: String?,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Long,
    val views: Long,
    val downloads: Long,
    val likes: Long,
    val user: String,
    @SerializedName("user_id") val userId: Long
)
