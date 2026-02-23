package com.rahul.wallcraft.domain.repository

import androidx.paging.PagingData
import com.rahul.wallcraft.domain.model.Category
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.model.WallpaperSource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getWallpapers(category: String? = null, source: WallpaperSource? = null): Flow<PagingData<Wallpaper>>
    fun searchWallpapers(query: String): Flow<PagingData<Wallpaper>>
    fun getEditorPicks(): Flow<PagingData<Wallpaper>>
    suspend fun getCategories(): List<Category>
    suspend fun getWallpaperById(id: String): Wallpaper?
}
