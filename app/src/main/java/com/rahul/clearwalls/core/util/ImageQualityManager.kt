package com.rahul.clearwalls.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rahul.clearwalls.domain.model.ImageQuality
import com.rahul.clearwalls.domain.model.Wallpaper
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
            networkMonitor.getCurrentStatus() == NetworkStatus.WIFI -> ImageQuality.TWO_K
            else -> ImageQuality.HD
        }
    }

    fun getUrlForQuality(wallpaper: Wallpaper, quality: ImageQuality): String = when (quality) {
        ImageQuality.LOW -> wallpaper.lowUrl ?: wallpaper.thumbnailUrl
        ImageQuality.HD -> wallpaper.hdUrl ?: wallpaper.previewUrl
        ImageQuality.TWO_K -> wallpaper.twoKUrl ?: wallpaper.fullUrl
        ImageQuality.FOUR_K -> wallpaper.fourKUrl ?: wallpaper.fullUrl
    }
}
