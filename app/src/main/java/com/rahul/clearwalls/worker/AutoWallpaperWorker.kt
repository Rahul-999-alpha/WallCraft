package com.rahul.clearwalls.worker

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
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
import okhttp3.OkHttpClient
import okhttp3.Request

@HiltWorker
class AutoWallpaperWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val favoriteDao: FavoriteDao,
    private val okHttpClient: OkHttpClient
) : CoroutineWorker(context, params) {

    companion object {
        private const val MAX_BITMAP_SIZE = 4096
    }

    override suspend fun doWork(): Result {
        return try {
            val favorites = favoriteDao.getAllFavorites().first()
            if (favorites.isEmpty()) return Result.success()

            val random = favorites.random()
            val imageUrl = random.fullUrl

            withContext(Dispatchers.IO) {
                val wallpaperManager = WallpaperManager.getInstance(applicationContext)

                // Download via OkHttp (configured timeouts); response is always closed via use{}.
                val request = Request.Builder().url(imageUrl).get().build()
                val bytes = okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@withContext
                    response.body?.bytes() ?: return@withContext
                }

                // Decode with a sample size so large favourites don't OOM the worker.
                val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, boundsOptions)
                val decodeOptions = BitmapFactory.Options().apply {
                    inSampleSize = calculateSampleSize(boundsOptions.outWidth, boundsOptions.outHeight)
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions)
                    ?: return@withContext

                try {
                    val target = inputData.getString("target") ?: "both"
                    when (target) {
                        "home" -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                        "lock" -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                        else -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                    }
                } finally {
                    bitmap.recycle()
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun calculateSampleSize(width: Int, height: Int): Int {
        var inSampleSize = 1
        val maxDim = maxOf(width, height)
        if (maxDim > MAX_BITMAP_SIZE) {
            inSampleSize = (maxDim / MAX_BITMAP_SIZE).coerceAtLeast(1)
        }
        return inSampleSize
    }
}
