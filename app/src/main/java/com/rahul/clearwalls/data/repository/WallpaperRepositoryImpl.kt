package com.rahul.clearwalls.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao
import com.rahul.clearwalls.data.local.dao.FavoriteDao
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.paging.CuratedFirestorePagingSource
import com.rahul.clearwalls.data.remote.firebase.FirestoreDataSource
import com.rahul.clearwalls.domain.model.Category
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Serves the app's OWNED catalog from Firestore (seeded via tools/seed_wallpapers).
 *
 * Pexels/Unsplash were removed from the pipeline for the Play Store release: both
 * providers' API terms explicitly prohibit wallpaper apps (Pexels revokes keys;
 * Unsplash rejects production approval), so shipping them was a legal and
 * availability risk. Their paging sources and DTOs remain in the tree, disconnected,
 * in case a licensed source is ever added.
 */
@Singleton
class WallpaperRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestoreDataSource: FirestoreDataSource,
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

        /** Fallback when the categories collection is unreachable (offline first launch). */
        private val DEFAULT_CATEGORIES = listOf(
            Category("amoled", "amoled", "AMOLED Dark", isPinned = true),
            Category("nature", "nature", "Nature"),
            Category("abstract", "abstract", "Abstract"),
            Category("minimal", "minimal", "Minimal"),
            Category("space", "space", "Space"),
            Category("city", "city", "City"),
            Category("gradient", "gradient", "Gradient"),
            Category("texture", "texture", "Texture"),
            Category("art", "art", "Art"),
            Category("animals", "animals", "Animals"),
            Category("flowers", "flowers", "Flowers"),
            Category("technology", "technology", "Technology")
        )
    }

    override fun getWallpapers(
        category: String?,
        source: WallpaperSource?
    ): Flow<PagingData<Wallpaper>> = Pager(config = PAGING_CONFIG) {
        // All sources now resolve to the owned Firestore catalog. The `source`
        // parameter is kept for the interface; third-party branches were removed
        // (see class doc).
        CuratedFirestorePagingSource(firestore = firestore, category = category)
    }.flow

    override fun searchWallpapers(query: String): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            CuratedFirestorePagingSource(firestore = firestore, searchQuery = query)
        }.flow

    override fun getEditorPicks(): Flow<PagingData<Wallpaper>> =
        Pager(config = PAGING_CONFIG) {
            CuratedFirestorePagingSource(firestore = firestore, editorPicksOnly = true)
        }.flow

    override suspend fun getCategories(): List<Category> {
        val remote = firestoreDataSource.getCategories()
        return remote.ifEmpty { DEFAULT_CATEGORIES }
    }

    override suspend fun getWallpaperById(id: String): Wallpaper? {
        // Check cached wallpapers first (has all quality URLs)
        cachedWallpaperDao.getById(id)?.let { return it.toWallpaper() }
        // Fallback to favorites table
        favoriteDao.getById(id)?.let { return it.toWallpaper() }
        return null
    }

    override suspend fun trackDownload(wallpaper: Wallpaper) {
        // Third-party sources are disconnected; nothing to notify for owned content.
        // Kept for legacy favorites saved from the old pipeline.
        if (wallpaper.source == WallpaperSource.UNSPLASH) {
            Log.d("WallpaperRepository", "Skipping legacy Unsplash download tracking (source disconnected)")
        }
    }
}
