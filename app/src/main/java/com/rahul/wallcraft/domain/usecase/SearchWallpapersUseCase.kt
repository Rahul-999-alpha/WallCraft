package com.rahul.wallcraft.domain.usecase

import androidx.paging.PagingData
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWallpapersUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Wallpaper>> =
        repository.searchWallpapers(query)
}
