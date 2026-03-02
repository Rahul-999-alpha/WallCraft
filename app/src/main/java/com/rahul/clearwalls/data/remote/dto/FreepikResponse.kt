package com.rahul.clearwalls.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FreepikSearchResponse(
    val data: List<FreepikResource>,
    val meta: FreepikMeta?
)

data class FreepikResource(
    val id: Long,
    val title: String?,
    val description: String?,
    val url: String?,
    val filename: String?,
    val image: FreepikImage?
)

data class FreepikImage(
    val source: FreepikImageSource?
)

data class FreepikImageSource(
    val url: String?,
    val size: String?
)

data class FreepikMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("per_page") val perPage: Int,
    val total: Int
)
