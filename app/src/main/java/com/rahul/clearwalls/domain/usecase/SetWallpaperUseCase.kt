package com.rahul.clearwalls.domain.usecase

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

enum class WallpaperTarget {
    HOME, LOCK, BOTH
}

class SetWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUrl: String, target: WallpaperTarget): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context)
                val inputStream = URL(imageUrl).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                    ?: return@withContext Result.failure(Exception("Failed to decode image"))

                when (target) {
                    WallpaperTarget.HOME -> wallpaperManager.setBitmap(
                        bitmap, null, true, WallpaperManager.FLAG_SYSTEM
                    )
                    WallpaperTarget.LOCK -> wallpaperManager.setBitmap(
                        bitmap, null, true, WallpaperManager.FLAG_LOCK
                    )
                    WallpaperTarget.BOTH -> {
                        wallpaperManager.setBitmap(
                            bitmap, null, true,
                            WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                        )
                    }
                }
                bitmap.recycle()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
