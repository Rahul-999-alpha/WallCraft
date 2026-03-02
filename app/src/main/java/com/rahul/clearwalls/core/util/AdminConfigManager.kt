package com.rahul.clearwalls.core.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class AdConfig(
    val bannerEnabled: Boolean = true,
    val nativeEnabled: Boolean = true,
    val interstitialEnabled: Boolean = true,
    val rewardedEnabled: Boolean = true,
    val nativeAdInterval: Int = 12,
    val interstitialCooldownMs: Long = 60_000L,
    val interstitialSetInterval: Int = 5,
    val interstitialDownloadInterval: Int = 3
)

data class RefreshConfig(
    val autoRefreshEnabled: Boolean = true,
    val refreshIntervalHours: Long = 11L
)

data class ContentConfig(
    val featuredCategory: String = "nature",
    val premiumEnabled: Boolean = false
)

@Singleton
class AdminConfigManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_BANNER_ENABLED = booleanPreferencesKey("admin_banner_enabled")
        private val KEY_NATIVE_ENABLED = booleanPreferencesKey("admin_native_enabled")
        private val KEY_INTERSTITIAL_ENABLED = booleanPreferencesKey("admin_interstitial_enabled")
        private val KEY_REWARDED_ENABLED = booleanPreferencesKey("admin_rewarded_enabled")
        private val KEY_NATIVE_AD_INTERVAL = intPreferencesKey("admin_native_interval")
        private val KEY_INTERSTITIAL_COOLDOWN = longPreferencesKey("admin_interstitial_cooldown")
        private val KEY_INTERSTITIAL_SET_INTERVAL = intPreferencesKey("admin_interstitial_set_interval")
        private val KEY_INTERSTITIAL_DOWNLOAD_INTERVAL = intPreferencesKey("admin_interstitial_dl_interval")
        private val KEY_AUTO_REFRESH = booleanPreferencesKey("admin_auto_refresh")
        private val KEY_REFRESH_INTERVAL = longPreferencesKey("admin_refresh_interval")
        private val KEY_FEATURED_CATEGORY = booleanPreferencesKey("admin_featured_category")
        private val KEY_PREMIUM_ENABLED = booleanPreferencesKey("admin_premium_enabled")
        private val KEY_ADMIN_AUTHENTICATED = booleanPreferencesKey("admin_authenticated")
    }

    val adConfig: Flow<AdConfig> = dataStore.data.map { prefs ->
        AdConfig(
            bannerEnabled = prefs[KEY_BANNER_ENABLED] ?: true,
            nativeEnabled = prefs[KEY_NATIVE_ENABLED] ?: true,
            interstitialEnabled = prefs[KEY_INTERSTITIAL_ENABLED] ?: true,
            rewardedEnabled = prefs[KEY_REWARDED_ENABLED] ?: true,
            nativeAdInterval = prefs[KEY_NATIVE_AD_INTERVAL] ?: 12,
            interstitialCooldownMs = prefs[KEY_INTERSTITIAL_COOLDOWN] ?: 60_000L,
            interstitialSetInterval = prefs[KEY_INTERSTITIAL_SET_INTERVAL] ?: 5,
            interstitialDownloadInterval = prefs[KEY_INTERSTITIAL_DOWNLOAD_INTERVAL] ?: 3
        )
    }

    val refreshConfig: Flow<RefreshConfig> = dataStore.data.map { prefs ->
        RefreshConfig(
            autoRefreshEnabled = prefs[KEY_AUTO_REFRESH] ?: true,
            refreshIntervalHours = prefs[KEY_REFRESH_INTERVAL] ?: 11L
        )
    }

    val isAdminAuthenticated: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ADMIN_AUTHENTICATED] ?: false
    }

    val premiumEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_PREMIUM_ENABLED] ?: false
    }

    suspend fun updateAdConfig(config: AdConfig) {
        dataStore.edit { prefs ->
            prefs[KEY_BANNER_ENABLED] = config.bannerEnabled
            prefs[KEY_NATIVE_ENABLED] = config.nativeEnabled
            prefs[KEY_INTERSTITIAL_ENABLED] = config.interstitialEnabled
            prefs[KEY_REWARDED_ENABLED] = config.rewardedEnabled
            prefs[KEY_NATIVE_AD_INTERVAL] = config.nativeAdInterval
            prefs[KEY_INTERSTITIAL_COOLDOWN] = config.interstitialCooldownMs
            prefs[KEY_INTERSTITIAL_SET_INTERVAL] = config.interstitialSetInterval
            prefs[KEY_INTERSTITIAL_DOWNLOAD_INTERVAL] = config.interstitialDownloadInterval
        }
    }

    suspend fun updateRefreshConfig(config: RefreshConfig) {
        dataStore.edit { prefs ->
            prefs[KEY_AUTO_REFRESH] = config.autoRefreshEnabled
            prefs[KEY_REFRESH_INTERVAL] = config.refreshIntervalHours
        }
    }

    suspend fun setAdminAuthenticated(authenticated: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ADMIN_AUTHENTICATED] = authenticated
        }
    }

    suspend fun setPremiumEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_PREMIUM_ENABLED] = enabled
        }
    }
}
