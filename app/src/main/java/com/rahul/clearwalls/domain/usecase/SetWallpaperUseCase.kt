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
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

enum class WallpaperTarget {
    HOME, LOCK, BOTH
}

class SetWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
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

                // Download once via OkHttp (inherits configured connect/read timeouts). The
                // previous code opened the URL twice (bounds + decode) with no timeout; we now
                // decode both passes from the same in-memory bytes.
                val bytes = try {
                    val request = Request.Builder().url(imageUrl).get().build()
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            return@withContext Result.failure(
                                IOException("Failed to download image: HTTP ${response.code}")
                            )
                        }
                        response.body?.bytes()
                            ?: return@withContext Result.failure(IOException("Empty response body"))
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to download image", e)
                    return@withContext Result.failure(
                        IOException("Failed to download image: ${e.message}")
                    )
                }

                // Decode with size limits to avoid OOM.
                val boundsOptions = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, boundsOptions)

                val scale = calculateSampleSize(boundsOptions.outWidth, boundsOptions.outHeight)
                val decodeOptions = BitmapFactory.Options().apply {
                    inSampleSize = scale
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOptions)

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
