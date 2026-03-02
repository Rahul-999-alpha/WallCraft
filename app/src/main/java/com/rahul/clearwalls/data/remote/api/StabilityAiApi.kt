package com.rahul.clearwalls.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StabilityAiApi {
    @Multipart
    @POST("v2beta/stable-image/generate/core")
    suspend fun generateImage(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "image/*",
        @Part("prompt") prompt: RequestBody,
        @Part("output_format") outputFormat: RequestBody,
        @Part("aspect_ratio") aspectRatio: RequestBody
    ): ResponseBody
}
