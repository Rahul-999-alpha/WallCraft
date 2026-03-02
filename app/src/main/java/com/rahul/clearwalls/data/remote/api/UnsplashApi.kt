package com.rahul.clearwalls.data.remote.api

import com.rahul.clearwalls.data.remote.dto.UnsplashSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("orientation") orientation: String = "portrait",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): UnsplashSearchResponse

    @GET("photos/{id}/download")
    suspend fun trackDownload(
        @Header("Authorization") authorization: String,
        @Path("id") photoId: String
    )
}
