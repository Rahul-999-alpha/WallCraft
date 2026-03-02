package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.PinterestSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PinterestApi {
    @GET("v5/search/pins")
    suspend fun searchPins(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("page_size") pageSize: Int = 20,
        @Query("bookmark") bookmark: String? = null
    ): PinterestSearchResponse
}
