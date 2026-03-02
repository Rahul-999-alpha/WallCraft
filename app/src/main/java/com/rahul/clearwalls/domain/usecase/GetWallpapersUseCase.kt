package com.rahul.clearwalls.domain.usecase

import androidx.paging.PagingData
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWallpapersUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    operator fun invoke(
        category: String? = null,
        source: WallpaperSource? = null
    ): Flow<PagingData<Wallpaper>> = repository.getWallpapers(category, source)
}
