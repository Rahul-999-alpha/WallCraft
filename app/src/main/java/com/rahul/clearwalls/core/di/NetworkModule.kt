package com.rahul.clearwalls.core.di

import android.content.Context
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.data.remote.api.FreepikApi
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.PinterestApi
import com.rahul.clearwalls.data.remote.api.PixabayApi
import com.rahul.clearwalls.data.remote.api.StabilityAiApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.data.remote.api.WallhavenApi
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

    @Provides
    @Singleton
    fun providePixabayApi(okHttpClient: OkHttpClient): PixabayApi =
        Retrofit.Builder()
            .baseUrl(Constants.PIXABAY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayApi::class.java)

    @Provides
    @Singleton
    fun provideWallhavenApi(okHttpClient: OkHttpClient): WallhavenApi =
        Retrofit.Builder()
            .baseUrl(Constants.WALLHAVEN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallhavenApi::class.java)

    @Provides
    @Singleton
    fun provideStabilityAiApi(okHttpClient: OkHttpClient): StabilityAiApi {
        val aiClient = okHttpClient.newBuilder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.STABILITY_AI_BASE_URL)
            .client(aiClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StabilityAiApi::class.java)
    }

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

    @Provides
    @Singleton
    fun providePinterestApi(okHttpClient: OkHttpClient): PinterestApi =
        Retrofit.Builder()
            .baseUrl(Constants.PINTEREST_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PinterestApi::class.java)

    @Provides
    @Singleton
    fun provideFreepikApi(okHttpClient: OkHttpClient): FreepikApi =
        Retrofit.Builder()
            .baseUrl(Constants.FREEPIK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreepikApi::class.java)
}
