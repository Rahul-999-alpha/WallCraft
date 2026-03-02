package com.rahul.clearwalls.presentation.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.clearwalls.domain.model.ImageQuality
import com.rahul.clearwalls.presentation.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {
        private val KEY_IMAGE_QUALITY = stringPreferencesKey("image_quality")
        private val KEY_DATA_SAVER = booleanPreferencesKey("data_saver")
        private val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        private val KEY_AUTO_WALLPAPER = booleanPreferencesKey("auto_wallpaper")
        private val KEY_AUTO_WALLPAPER_INTERVAL = stringPreferencesKey("auto_wallpaper_interval")
    }

    val imageQuality: StateFlow<ImageQuality> = dataStore.data
        .map { prefs ->
            prefs[KEY_IMAGE_QUALITY]?.let {
                try { ImageQuality.valueOf(it) } catch (_: Exception) { ImageQuality.HD }
            } ?: ImageQuality.HD
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ImageQuality.HD)

    val dataSaverEnabled: StateFlow<Boolean> = dataStore.data
        .map { prefs -> prefs[KEY_DATA_SAVER] ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeMode: StateFlow<ThemeMode> = dataStore.data
        .map { prefs ->
            prefs[KEY_THEME_MODE]?.let {
                try { ThemeMode.valueOf(it) } catch (_: Exception) { ThemeMode.SYSTEM }
            } ?: ThemeMode.SYSTEM
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val autoWallpaperEnabled: StateFlow<Boolean> = dataStore.data
        .map { prefs -> prefs[KEY_AUTO_WALLPAPER] ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val autoWallpaperInterval: StateFlow<String> = dataStore.data
        .map { prefs -> prefs[KEY_AUTO_WALLPAPER_INTERVAL] ?: "12h" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "12h")

    fun setImageQuality(quality: ImageQuality) {
        viewModelScope.launch {
            dataStore.edit { it[KEY_IMAGE_QUALITY] = quality.name }
        }
    }

    fun setDataSaver(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[KEY_DATA_SAVER] = enabled }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            dataStore.edit { it[KEY_THEME_MODE] = mode.name }
        }
    }

    fun setAutoWallpaper(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[KEY_AUTO_WALLPAPER] = enabled }
        }
    }

    fun setAutoWallpaperInterval(interval: String) {
        viewModelScope.launch {
            dataStore.edit { it[KEY_AUTO_WALLPAPER_INTERVAL] = interval }
        }
    }
}
