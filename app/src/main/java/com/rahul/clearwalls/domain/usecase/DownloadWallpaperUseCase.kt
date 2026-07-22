package com.rahul.clearwalls.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DownloadWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
) {
    companion object {
        private const val TAG = "DownloadWallpaper"
    }

    suspend operator fun invoke(imageUrl: String, fileName: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting download: $imageUrl")

                // Download image bytes via OkHttp (inherits configured connect/read timeouts).
                val bytes = try {
                    val request = Request.Builder().url(imageUrl).get().build()
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            return@withContext Result.failure(
                                IOException("Download failed: HTTP ${response.code}")
                            )
                        }
                        response.body?.bytes()
                            ?: return@withContext Result.failure(IOException("Empty response body"))
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to download image from URL", e)
                    return@withContext Result.failure(
                        IOException("Failed to download image: ${e.message}")
                    )
                }

                Log.d(TAG, "Downloaded ${bytes.size} bytes")

                // MediaStore RELATIVE_PATH / IS_PENDING are API 29+ only. minSdk is 26, so
                // version-gate: pre-29 writes to the public Pictures dir + MediaScanner.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveViaMediaStore(bytes, fileName)
                } else {
                    saveToLegacyStorage(bytes, fileName)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
                Result.failure(e)
            }
        }

    // Android 10+ (API 29+): scoped storage via MediaStore.
    private fun saveViaMediaStore(bytes: ByteArray, fileName: String): Result<String> {
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
            return Result.failure(Exception("Failed to create media entry"))
        }

        Log.d(TAG, "Created MediaStore entry: $uri")

        resolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(bytes)
            outputStream.flush()
        } ?: run {
            Log.e(TAG, "Failed to open output stream")
            resolver.delete(uri, null, null)
            return Result.failure(IOException("Failed to write image data"))
        }

        // Publish the pending entry so it becomes visible in the gallery.
        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)

        Log.d(TAG, "Download completed successfully: $uri")
        return Result.success(uri.toString())
    }

    // Android 8–9 (API 26–28): direct file write + MediaScanner (uses WRITE_EXTERNAL_STORAGE).
    @Suppress("DEPRECATION")
    private fun saveToLegacyStorage(bytes: ByteArray, fileName: String): Result<String> {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val targetDir = File(picturesDir, "ClearWalls")
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory: ${targetDir.absolutePath}")
            return Result.failure(IOException("Failed to create download directory"))
        }

        val file = File(targetDir, "$fileName.jpg")
        file.outputStream().use { outputStream ->
            outputStream.write(bytes)
            outputStream.flush()
        }

        // Make the file visible to the gallery on legacy storage.
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            arrayOf("image/jpeg"),
            null
        )

        Log.d(TAG, "Download completed successfully (legacy): ${file.absolutePath}")
        return Result.success(file.absolutePath)
    }
}
