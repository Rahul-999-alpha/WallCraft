package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.WallhavenResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WallhavenApi {
    @GET("v1/search")
    suspend fun searchWallpapers(
        @Header("X-API-Key") apiKey: String,
        @Query("q") query: String = "",
        @Query("categories") categories: String = "100",
        @Query("purity") purity: String = "100",
        @Query("atleast") atleast: String = "1080x1920",
        @Query("ratios") ratios: String = "9x16,9x18,9x19,9x20",
        @Query("sorting") sorting: String = "relevance",
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1
    ): WallhavenResponse
}
