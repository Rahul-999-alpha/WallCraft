package com.rahul.clearwalls.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao
import com.rahul.clearwalls.data.local.dao.FavoriteDao
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.paging.MergedWallpaperPagingSource
import com.rahul.clearwalls.data.paging.PexelsPagingSource
import com.rahul.clearwalls.data.paging.UnsplashPagingSource
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.domain.model.Category
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperRepositoryImpl @Inject constructor(
    private val pexelsApi: PexelsApi,
    private val unsplashApi: UnsplashApi,
    private val cachedWallpaperDao: CachedWallpaperDao,
    private val favoriteDao: FavoriteDao
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
            // DISABLED — no API keys. Uncomment when keys are obtained.
            // WallpaperSource.PIXABAY -> PixabayPagingSource(api = pixabayApi, category = category)
            // WallpaperSource.WALLHAVEN -> WallhavenPagingSource(api = wallhavenApi, query = category ?: "")
            WallpaperSource.PEXELS -> PexelsPagingSource(api = pexelsApi, query = category ?: "wallpaper")
            WallpaperSource.UNSPLASH -> UnsplashPagingSource(api = unsplashApi, query = category ?: "wallpaper")
            else -> MergedWallpaperPagingSource(
                pexelsApi = pexelsApi,
                unsplashApi = unsplashApi,
                category = category
            )
        }
    }.flow

    override fun searchWallpapers(query: String): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            MergedWallpaperPagingSource(
                pexelsApi = pexelsApi,
                unsplashApi = unsplashApi,
                query = query
            )
        }.flow

    override fun getEditorPicks(): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            // Was Pixabay editor's choice; now uses Pexels curated content
            PexelsPagingSource(api = pexelsApi, query = "curated wallpaper")
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
        Category("art", "art", "Art"),
        Category("motivational", "motivational quotes wallpaper", "Motivational"),
        Category("gradient", "gradient background", "Gradient"),
        Category("texture", "texture pattern", "Texture")
    )

    override suspend fun getWallpaperById(id: String): Wallpaper? {
        // Check cached wallpapers first (has all quality URLs)
        cachedWallpaperDao.getById(id)?.let { return it.toWallpaper() }
        // Fallback to favorites table
        favoriteDao.getById(id)?.let { return it.toWallpaper() }
        return null
    }
}
