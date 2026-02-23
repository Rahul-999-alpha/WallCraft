package com.rahul.wallcraft.domain.usecase

import androidx.paging.PagingData
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.model.WallpaperSource
import com.rahul.wallcraft.domain.repository.WallpaperRepository
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
