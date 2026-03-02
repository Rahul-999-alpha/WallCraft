package com.rahul.clearwalls.domain.usecase

import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(wallpaper: Wallpaper): Boolean =
        repository.toggleFavorite(wallpaper)
}
