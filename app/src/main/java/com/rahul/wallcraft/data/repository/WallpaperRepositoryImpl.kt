package com.rahul.wallcraft.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rahul.wallcraft.data.paging.MergedWallpaperPagingSource
import com.rahul.wallcraft.data.paging.PixabayPagingSource
import com.rahul.wallcraft.data.paging.WallhavenPagingSource
import com.rahul.wallcraft.data.remote.api.PixabayApi
import com.rahul.wallcraft.data.remote.api.WallhavenApi
import com.rahul.wallcraft.domain.model.Category
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.model.WallpaperSource
import com.rahul.wallcraft.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperRepositoryImpl @Inject constructor(
    private val pixabayApi: PixabayApi,
    private val wallhavenApi: WallhavenApi
) : WallpaperRepository {

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            enablePlaceholders = false,
            initialLoadSize = 20
        )
    }

    override fun getWallpapers(
        category: String?,
        source: WallpaperSource?
    ): Flow<PagingData<Wallpaper>> = Pager(config = PAGING_CONFIG) {
        when (source) {
            WallpaperSource.PIXABAY -> PixabayPagingSource(
                api = pixabayApi,
                category = category
            )
            WallpaperSource.WALLHAVEN -> WallhavenPagingSource(
                api = wallhavenApi,
                query = category ?: ""
            )
            else -> MergedWallpaperPagingSource(
                pixabayApi = pixabayApi,
                wallhavenApi = wallhavenApi,
                category = category
            )
        }
    }.flow

    override fun searchWallpapers(query: String): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            MergedWallpaperPagingSource(
                pixabayApi = pixabayApi,
                wallhavenApi = wallhavenApi,
                query = query
            )
        }.flow

    override fun getEditorPicks(): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            PixabayPagingSource(
                api = pixabayApi,
                editorsChoice = true
            )
        }.flow

    override suspend fun getCategories(): List<Category> = listOf(
        Category("amoled", "amoled", "AMOLED Dark", isPinned = true),
        Category("nature", "nature", "Nature"),
        Category("abstract", "abstract", "Abstract"),
        Category("minimal", "minimal", "Minimal"),
        Category("anime", "anime", "Anime"),
        Category("cars", "cars", "Cars"),
        Category("space", "space", "Space"),
        Category("city", "city", "City"),
        Category("animals", "animals", "Animals"),
        Category("sports", "sports", "Sports"),
        Category("flowers", "flowers", "Flowers"),
        Category("travel", "travel", "Travel"),
        Category("food", "food", "Food"),
        Category("music", "music", "Music"),
        Category("technology", "technology", "Technology"),
        Category("art", "art", "Art")
    )

    override suspend fun getWallpaperById(id: String): Wallpaper? = null
}
