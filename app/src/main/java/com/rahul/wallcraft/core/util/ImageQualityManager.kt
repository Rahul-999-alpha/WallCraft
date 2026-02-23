package com.rahul.wallcraft.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rahul.wallcraft.domain.model.ImageQuality
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageQualityManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkMonitor: NetworkMonitor,
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_QUALITY_OVERRIDE = stringPreferencesKey("image_quality_override")
        private val KEY_DATA_SAVER = stringPreferencesKey("data_saver_enabled")
    }

    val qualityOverride: Flow<ImageQuality?> = dataStore.data.map { prefs ->
        prefs[KEY_QUALITY_OVERRIDE]?.let { ImageQuality.valueOf(it) }
    }

    suspend fun setQualityOverride(quality: ImageQuality?) {
        dataStore.edit { prefs ->
            if (quality != null) {
                prefs[KEY_QUALITY_OVERRIDE] = quality.name
            } else {
                prefs.remove(KEY_QUALITY_OVERRIDE)
            }
        }
    }

    suspend fun getRecommendedQuality(): ImageQuality {
        val override = qualityOverride.first()
        if (override != null) return override

        return when {
            networkMonitor.isMetered() -> ImageQuality.LOW
            networkMonitor.getCurrentStatus() == NetworkStatus.WIFI -> ImageQuality.HIGH
            else -> ImageQuality.MEDIUM
        }
    }

    fun getUrlForQuality(
        thumbnailUrl: String,
        previewUrl: String,
        fullUrl: String,
        quality: ImageQuality
    ): String = when (quality) {
        ImageQuality.LOW -> thumbnailUrl
        ImageQuality.MEDIUM -> previewUrl
        ImageQuality.HIGH -> fullUrl
        ImageQuality.ORIGINAL -> fullUrl
    }
}
