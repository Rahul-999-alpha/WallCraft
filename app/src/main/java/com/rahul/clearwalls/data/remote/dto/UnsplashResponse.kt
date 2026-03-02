package com.rahul.clearwalls.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UnsplashSearchResponse(
    val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    val results: List<UnsplashPhoto>
)

data class UnsplashPhoto(
    val id: String,
    val width: Int,
    val height: Int,
    val color: String?,
    val description: String?,
    @SerializedName("alt_description") val altDescription: String?,
    val urls: UnsplashUrls,
    val links: UnsplashLinks,
    val user: UnsplashUser
)

data class UnsplashUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class UnsplashLinks(
    val self: String,
    val html: String,
    val download: String,
    @SerializedName("download_location") val downloadLocation: String
)

data class UnsplashUser(
    val id: String,
    val username: String,
    val name: String,
    @SerializedName("portfolio_url") val portfolioUrl: String?
)
