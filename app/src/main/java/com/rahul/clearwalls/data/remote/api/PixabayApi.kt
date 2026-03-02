package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String = "",
        @Query("image_type") imageType: String = "photo",
        @Query("orientation") orientation: String = "vertical",
        @Query("min_width") minWidth: Int = 1080,
        @Query("min_height") minHeight: Int = 1920,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("category") category: String? = null,
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("order") order: String = "popular",
        @Query("safesearch") safeSearch: Boolean = true
    ): PixabayResponse
}
