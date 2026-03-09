package com.rahul.clearwalls.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao
import com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.domain.model.Wallpaper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WallpaperRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val pexelsApi: PexelsApi,
    private val cachedWallpaperDao: CachedWallpaperDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val wallpapers = mutableListOf<Wallpaper>()

            // DISABLED — no Pixabay API key. Uncomment when key is obtained.
            // try { ... pixabayApi fetch ... } catch (_: Exception) {}

            // Fetch from Pexels
            try {
                val pexelsKey = BuildConfig.PEXELS_API_KEY
                if (pexelsKey.isNotBlank()) {
                    val response = pexelsApi.getCurated(apiKey = pexelsKey, perPage = 20)
                    wallpapers.addAll(response.photos.map { it.toWallpaper() })
                }
            } catch (_: Exception) {}

            // Cache results
            val now = System.currentTimeMillis()
            val entities = wallpapers.map { wp ->
                CachedWallpaperEntity(
                    id = wp.id,
                    source = wp.source.name,
                    title = wp.title,
                    thumbnailUrl = wp.thumbnailUrl,
                    previewUrl = wp.previewUrl,
                    fullUrl = wp.fullUrl,
                    width = wp.width,
                    height = wp.height,
                    dominantColor = wp.dominantColor,
                    tags = wp.tags.joinToString(","),
                    category = wp.category,
                    isAmoled = wp.isAmoled,
                    lowUrl = wp.lowUrl,
                    hdUrl = wp.hdUrl,
                    twoKUrl = wp.twoKUrl,
                    fourKUrl = wp.fourKUrl,
                    photographer = wp.photographer,
                    photographerUrl = wp.photographerUrl,
                    sourceUrl = wp.sourceUrl,
                    cachedAt = now
                )
            }
            cachedWallpaperDao.insertAll(entities)

            // Clean old cache (older than 48 hours)
            cachedWallpaperDao.deleteOlderThan(now - 48 * 60 * 60 * 1000L)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
