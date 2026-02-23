package com.rahul.wallcraft.presentation.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.wallcraft.domain.model.ImageQuality
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
    }

    val imageQuality: StateFlow<ImageQuality> = dataStore.data
        .map { prefs ->
            prefs[KEY_IMAGE_QUALITY]?.let { ImageQuality.valueOf(it) } ?: ImageQuality.MEDIUM
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ImageQuality.MEDIUM)

    val dataSaverEnabled: StateFlow<Boolean> = dataStore.data
        .map { prefs -> prefs[KEY_DATA_SAVER] ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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
}
