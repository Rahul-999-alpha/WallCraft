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
}

@HiltViewModel
class WallpaperDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val wallpaperRepository: WallpaperRepository,
    private val favoriteRepository: FavoriteRepository,
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val downloadWallpaperUseCase: DownloadWallpaperUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val wallpaperId: String = savedStateHandle.get<String>("wallpaperId") ?: ""

    private val _wallpaper = MutableStateFlow<Wallpaper?>(null)
    val wallpaper: StateFlow<Wallpaper?> = _wallpaper.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    init {
        loadWallpaper()
    }

    private fun loadWallpaper() {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(wallpaperId)
        }
    }

    fun setWallpaperData(wallpaper: Wallpaper) {
        _wallpaper.value = wallpaper
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(wallpaper.id)
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
                }
                .onFailure {
                    _events.emit(DetailEvent.ShowMessage("Failed to download: ${it.message}"))
                }
            _isLoading.value = false
        }
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
