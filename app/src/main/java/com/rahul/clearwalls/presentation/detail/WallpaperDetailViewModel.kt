package com.rahul.clearwalls.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.clearwalls.domain.model.ImageQuality
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.repository.FavoriteRepository
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import com.rahul.clearwalls.domain.usecase.DownloadWallpaperUseCase
import com.rahul.clearwalls.domain.usecase.SetWallpaperUseCase
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase
import com.rahul.clearwalls.domain.usecase.WallpaperTarget
import android.util.Log
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.core.util.AdManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailEvent {
    data class ShowMessage(val message: String) : DetailEvent()
    data object WallpaperSet : DetailEvent()
    data object WallpaperDownloaded : DetailEvent()
    data object ShowInterstitial : DetailEvent()
    data class ShowRewardedForPremium(val quality: ImageQuality) : DetailEvent()
}

@HiltViewModel
class WallpaperDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val wallpaperRepository: WallpaperRepository,
    private val favoriteRepository: FavoriteRepository,
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val downloadWallpaperUseCase: DownloadWallpaperUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val adManager: AdManager
) : ViewModel() {

    companion object {
        private const val TAG = "WallpaperDetailVM"
        // In-memory cache set before navigation for fast wallpaper data transfer
        var pendingWallpaper: Wallpaper? = null
    }

    private val wallpaperId: String = savedStateHandle.get<String>("wallpaperId") ?: ""

    private val _wallpaper = MutableStateFlow<Wallpaper?>(null)
    val wallpaper: StateFlow<Wallpaper?> = _wallpaper.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    // Interstitial gating counters (moved from old AdManager)
    private var setWallpaperCount = 0
    private var downloadCount = 0
    private var lastInterstitialTime = 0L
    private val sessionStart = System.currentTimeMillis()

    init {
        loadWallpaper()
    }

    private fun loadWallpaper() {
        viewModelScope.launch {
            // 1. Fast path: use pending wallpaper from navigation cache
            val pending = pendingWallpaper
            if (pending != null && pending.id == wallpaperId) {
                _wallpaper.value = pending
                pendingWallpaper = null
                Log.d(TAG, "Loaded wallpaper from navigation cache: ${pending.id}")
            } else {
                // 2. Fallback: load from database (cached wallpapers or favorites)
                Log.d(TAG, "Loading wallpaper from database: $wallpaperId")
                val dbWallpaper = wallpaperRepository.getWallpaperById(wallpaperId)
                if (dbWallpaper != null) {
                    _wallpaper.value = dbWallpaper
                    Log.d(TAG, "Loaded wallpaper from database: ${dbWallpaper.id}")
                } else {
                    Log.e(TAG, "Failed to load wallpaper: $wallpaperId - not found in cache or database")
                    _events.emit(DetailEvent.ShowMessage("Failed to load wallpaper data"))
                }
            }

            // Check favorite status
            _isFavorite.value = favoriteRepository.isFavorite(wallpaperId)
        }
    }

    private fun shouldShowInterstitial(count: Int, interval: Int): Boolean {
        val now = System.currentTimeMillis()
        if (now - sessionStart < Constants.AD_FIRST_SESSION_GRACE_MS) return false
        if (now - lastInterstitialTime < Constants.AD_INTERSTITIAL_COOLDOWN_MS) return false
        return count % interval == 0
    }

    fun showInterstitialAd() {
        adManager.showInterstitial {
            lastInterstitialTime = System.currentTimeMillis()
        }
    }

    fun setWallpaper(target: WallpaperTarget) {
        val wp = _wallpaper.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            setWallpaperUseCase(wp.fullUrl, target)
                .onSuccess {
                    _events.emit(DetailEvent.WallpaperSet)
                    _events.emit(DetailEvent.ShowMessage("Wallpaper set successfully!"))
                    setWallpaperCount++
                    if (shouldShowInterstitial(setWallpaperCount, Constants.AD_INTERSTITIAL_SET_INTERVAL)) {
                        _events.emit(DetailEvent.ShowInterstitial)
                    }
                }
                .onFailure {
                    _events.emit(DetailEvent.ShowMessage("Failed to set wallpaper: ${it.message}"))
                }
            _isLoading.value = false
        }
    }

    fun downloadWallpaper(quality: ImageQuality = ImageQuality.HD) {
        val wp = _wallpaper.value ?: return
        val url = when (quality) {
            ImageQuality.LOW -> wp.lowUrl ?: wp.previewUrl
            ImageQuality.HD -> wp.hdUrl ?: wp.fullUrl
            ImageQuality.TWO_K -> wp.twoKUrl ?: wp.fullUrl
            ImageQuality.FOUR_K -> wp.fourKUrl ?: wp.fullUrl
        }
        viewModelScope.launch {
            _isLoading.value = true
            downloadWallpaperUseCase(url, "clearwalls_${wp.id}_${quality.label}")
                .onSuccess {
                    _events.emit(DetailEvent.WallpaperDownloaded)
                    _events.emit(DetailEvent.ShowMessage("Wallpaper saved to gallery!"))
                    downloadCount++
                    if (shouldShowInterstitial(downloadCount, Constants.AD_INTERSTITIAL_DOWNLOAD_INTERVAL)) {
                        _events.emit(DetailEvent.ShowInterstitial)
                    }
                }
                .onFailure {
                    _events.emit(DetailEvent.ShowMessage("Failed to download: ${it.message}"))
                }
            _isLoading.value = false
        }
    }

    fun requestPremiumDownload(quality: ImageQuality) {
        viewModelScope.launch {
            _events.emit(DetailEvent.ShowRewardedForPremium(quality))
        }
    }

    fun showRewardedAd(quality: ImageQuality) {
        adManager.showRewarded(
            onRewarded = { downloadWallpaper(quality) },
            onDismissed = {}
        )
    }

    fun toggleFavorite() {
        val wp = _wallpaper.value ?: return
        viewModelScope.launch {
            val nowFavorite = toggleFavoriteUseCase(wp)
            _isFavorite.value = nowFavorite
            _wallpaper.value = wp.copy(isFavorite = nowFavorite)
        }
    }
}
