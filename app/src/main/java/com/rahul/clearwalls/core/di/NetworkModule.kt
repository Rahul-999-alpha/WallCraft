package com.rahul.clearwalls.core.di

import android.content.Context
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.StabilityAiApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheDir = File(context.cacheDir, "http_cache")
        val cache = Cache(cacheDir, Constants.HTTP_CACHE_SIZE)

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()
    }

    // DISABLED — no API keys. Uncomment when keys are obtained.
    // @Provides @Singleton
    // fun providePixabayApi(okHttpClient: OkHttpClient): PixabayApi = ...

    // @Provides @Singleton
    // fun provideWallhavenApi(okHttpClient: OkHttpClient): WallhavenApi = ...

    // DISABLED — Replaced by PuterAiService (Puter.js WebView bridge). No API key needed.
    // @Provides
    // @Singleton
    // fun provideStabilityAiApi(okHttpClient: OkHttpClient): StabilityAiApi {
    //     val aiClient = okHttpClient.newBuilder()
    //         .readTimeout(120, TimeUnit.SECONDS)
    //         .writeTimeout(120, TimeUnit.SECONDS)
    //         .build()
    //     return Retrofit.Builder()
    //         .baseUrl(Constants.STABILITY_AI_BASE_URL)
    //         .client(aiClient)
    //         .addConverterFactory(GsonConverterFactory.create())
    //         .build()
    //         .create(StabilityAiApi::class.java)
    // }

    @Provides
    @Singleton
    fun providePexelsApi(okHttpClient: OkHttpClient): PexelsApi =
        Retrofit.Builder()
            .baseUrl(Constants.PEXELS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApi::class.java)

    @Provides
    @Singleton
    fun provideUnsplashApi(okHttpClient: OkHttpClient): UnsplashApi =
        Retrofit.Builder()
            .baseUrl(Constants.UNSPLASH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)

    // DISABLED — no API keys. Uncomment when keys are obtained.
    // @Provides @Singleton
    // fun providePinterestApi(okHttpClient: OkHttpClient): PinterestApi = ...

    // @Provides @Singleton
    // fun provideFreepikApi(okHttpClient: OkHttpClient): FreepikApi = ...
}
