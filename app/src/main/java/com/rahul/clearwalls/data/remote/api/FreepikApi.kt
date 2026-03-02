package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.FreepikSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FreepikApi {
    @GET("v1/resources")
    suspend fun searchResources(
        @Header("x-freepik-api-key") apiKey: String,
        @Query("term") query: String,
        @Query("orientation") orientation: String = "portrait",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): FreepikSearchResponse
}
