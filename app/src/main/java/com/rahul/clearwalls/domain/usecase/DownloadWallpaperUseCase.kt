package com.rahul.clearwalls.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class DownloadWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "DownloadWallpaper"
    }

    suspend operator fun invoke(imageUrl: String, fileName: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting download: $imageUrl")

                // Download image bytes
                val bytes = try {
                    URL(imageUrl).openStream().use { inputStream ->
                        inputStream.readBytes()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to download image from URL", e)
                    return@withContext Result.failure(
                        IOException("Failed to download image: ${e.message}")
                    )
                }

                Log.d(TAG, "Downloaded ${bytes.size} bytes")

                // Use MediaStore API (Android 10+)
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "${Environment.DIRECTORY_PICTURES}/ClearWalls"
                    )
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: run {
                    Log.e(TAG, "Failed to create MediaStore entry")
                    return@withContext Result.failure(
                        Exception("Failed to create media entry")
                    )
                }

                Log.d(TAG, "Created MediaStore entry: $uri")

                // Write image data
                resolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(bytes)
                    outputStream.flush()
                } ?: run {
                    Log.e(TAG, "Failed to open output stream")
                    resolver.delete(uri, null, null)
                    return@withContext Result.failure(
                        IOException("Failed to write image data")
                    )
                }

                // Make file visible in gallery
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                Log.d(TAG, "Download completed successfully: $uri")
                Result.success(uri.toString())

            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
                Result.failure(e)
            }
        }
}
