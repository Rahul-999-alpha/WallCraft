package com.rahul.clearwalls.worker

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rahul.clearwalls.data.local.dao.FavoriteDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.net.URL

@HiltWorker
class AutoWallpaperWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val favoriteDao: FavoriteDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val favorites = favoriteDao.getAllFavorites().first()
            if (favorites.isEmpty()) return Result.success()

            val random = favorites.random()
            val imageUrl = random.fullUrl

            withContext(Dispatchers.IO) {
                val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                val inputStream = URL(imageUrl).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext

                val target = inputData.getString("target") ?: "both"
                when (target) {
                    "home" -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                    "lock" -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    else -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                }
                bitmap.recycle()
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
