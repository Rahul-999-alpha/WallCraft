package com.rahul.wallcraft.domain.usecase

import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(wallpaper: Wallpaper): Boolean =
        repository.toggleFavorite(wallpaper)
}
