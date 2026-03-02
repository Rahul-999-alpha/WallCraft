package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.PexelsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("orientation") orientation: String = "portrait",
        @Query("size") size: String = "large",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelsResponse

    @GET("v1/curated")
    suspend fun getCurated(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelsResponse
}
