package com.rahul.clearwalls.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PinterestSearchResponse(
    val items: List<PinterestPin>,
    val bookmark: String?
)

data class PinterestPin(
    val id: String,
    val title: String?,
    val description: String?,
    val link: String?,
    @SerializedName("dominant_color") val dominantColor: String?,
    val media: PinterestMedia?
)

data class PinterestMedia(
    val images: Map<String, PinterestImage>?
)

data class PinterestImage(
    val url: String,
    val width: Int,
    val height: Int
)
