package com.rahul.clearwalls.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class DownloadWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUrl: String, fileName: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(imageUrl).openStream()
                val bytes = inputStream.readBytes()

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
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                ) ?: return@withContext Result.failure(Exception("Failed to create media entry"))

                resolver.openOutputStream(uri)?.use { os ->
                    os.write(bytes)
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                Result.success(uri.toString())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
