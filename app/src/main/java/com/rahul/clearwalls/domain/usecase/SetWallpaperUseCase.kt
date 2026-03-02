package com.rahul.clearwalls.domain.usecase

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import kotlin.math.min

enum class WallpaperTarget {
    HOME, LOCK, BOTH
}

class SetWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "SetWallpaper"
        private const val MAX_BITMAP_SIZE = 4096 // Maximum dimension to avoid OOM
    }

    suspend operator fun invoke(imageUrl: String, target: WallpaperTarget): Result<Unit> =
        withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null
            try {
                Log.d(TAG, "Setting wallpaper from: $imageUrl for target: $target")

                val wallpaperManager = WallpaperManager.getInstance(context)

                // Download and decode bitmap
                val inputStream = try {
                    URL(imageUrl).openStream()
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to open connection to image URL", e)
                    return@withContext Result.failure(
                        IOException("Failed to download image: ${e.message}")
                    )
                }

                // Decode with size limits to avoid OOM
                bitmap = inputStream.use { stream ->
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }

                    // First, get dimensions
                    BitmapFactory.decodeStream(stream, null, options)

                    // Calculate sample size if image is too large
                    val scale = calculateSampleSize(options.outWidth, options.outHeight)

                    // Re-open stream and decode with sample size
                    URL(imageUrl).openStream().use { retryStream ->
                        val decodeOptions = BitmapFactory.Options().apply {
                            inSampleSize = scale
                            inPreferredConfig = Bitmap.Config.ARGB_8888
                        }
                        BitmapFactory.decodeStream(retryStream, null, decodeOptions)
                    }
                }

                if (bitmap == null) {
                    Log.e(TAG, "Failed to decode bitmap")
                    return@withContext Result.failure(
                        Exception("Failed to decode image")
                    )
                }

                Log.d(TAG, "Bitmap decoded: ${bitmap.width}x${bitmap.height}")

                // Set wallpaper based on target
                when (target) {
                    WallpaperTarget.HOME -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(
                                bitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            wallpaperManager.setBitmap(bitmap)
                        }
                        Log.d(TAG, "Home wallpaper set successfully")
                    }

                    WallpaperTarget.LOCK -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(
                                bitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            Log.d(TAG, "Lock wallpaper set successfully")
                        } else {
                            Log.w(TAG, "Lock wallpaper not supported on API < 24")
                            return@withContext Result.failure(
                                Exception("Lock wallpaper requires Android 7.0+")
                            )
                        }
                    }

                    WallpaperTarget.BOTH -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(
                                bitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                            )
                            Log.d(TAG, "Both wallpapers set successfully")
                        } else {
                            @Suppress("DEPRECATION")
                            wallpaperManager.setBitmap(bitmap)
                            Log.d(TAG, "Home wallpaper set (lock not supported)")
                        }
                    }
                }

                Result.success(Unit)

            } catch (e: SecurityException) {
                Log.e(TAG, "Permission denied", e)
                Result.failure(SecurityException("Permission denied: ${e.message}"))
            } catch (e: IOException) {
                Log.e(TAG, "IO error while setting wallpaper", e)
                Result.failure(IOException("Failed to set wallpaper: ${e.message}"))
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error", e)
                Result.failure(e)
            } finally {
                // Always recycle bitmap to prevent memory leaks
                bitmap?.let {
                    if (!it.isRecycled) {
                        it.recycle()
                        Log.d(TAG, "Bitmap recycled")
                    }
                }
            }
        }

    private fun calculateSampleSize(width: Int, height: Int): Int {
        var inSampleSize = 1
        val maxDim = maxOf(width, height)

        if (maxDim > MAX_BITMAP_SIZE) {
            inSampleSize = (maxDim / MAX_BITMAP_SIZE).coerceAtLeast(1)
        }

        Log.d(TAG, "Calculated sample size: $inSampleSize for dimensions ${width}x${height}")
        return inSampleSize
    }
}
