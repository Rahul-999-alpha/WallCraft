package com.rahul.clearwalls.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PexelsResponse(
    val page: Int,
    @SerializedName("per_page") val perPage: Int,
    val photos: List<PexelsPhoto>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("next_page") val nextPage: String?
)

data class PexelsPhoto(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    @SerializedName("photographer_url") val photographerUrl: String,
    @SerializedName("photographer_id") val photographerId: Long,
    @SerializedName("avg_color") val avgColor: String?,
    val src: PexelsSrc,
    val alt: String?
)

data class PexelsSrc(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
