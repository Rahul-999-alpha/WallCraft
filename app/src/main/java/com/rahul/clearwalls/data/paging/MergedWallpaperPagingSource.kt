package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.domain.model.Wallpaper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MergedWallpaperPagingSource(
    private val pexelsApi: PexelsApi,
    private val unsplashApi: UnsplashApi,
    private val query: String = "",
    private val category: String? = null
) : PagingSource<Int, Wallpaper>() {

    companion object {
        private const val TAG = "MergedPagingSource"

        fun isValidApiKey(key: String): Boolean {
            return key.isNotBlank() && !key.startsWith("your_") && !key.endsWith("_here")
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        val page = params.key ?: 1
        return try {
            val searchQuery = query.ifBlank { category ?: "wallpaper" }

            val sources = coroutineScope {
                // DISABLED — no API keys. Uncomment when keys are obtained.
                // val pixabayDeferred = async { ... }
                // val wallhavenDeferred = async { ... }
                // val freepikDeferred = async { ... }

                val pexelsDeferred = async {
                    try {
                        val apiKey = BuildConfig.PEXELS_API_KEY
                        if (!isValidApiKey(apiKey)) {
                            Log.w(TAG, "Pexels: skipped (invalid key)")
                            return@async emptyList()
                        }
                        val response = pexelsApi.searchPhotos(
                            apiKey = apiKey,
                            query = searchQuery,
                            page = page,
                            perPage = 15
                        )
                        Log.d(TAG, "Pexels: loaded ${response.photos.size} wallpapers")
                        response.photos.map { it.toWallpaper() }
                    } catch (e: Exception) {
                        Log.e(TAG, "Pexels: failed - ${e.message}")
                        emptyList()
                    }
                }

                val unsplashDeferred = async {
                    try {
                        val accessKey = BuildConfig.UNSPLASH_ACCESS_KEY
                        if (!isValidApiKey(accessKey)) {
                            Log.w(TAG, "Unsplash: skipped (invalid key)")
                            return@async emptyList()
                        }
                        val response = unsplashApi.searchPhotos(
                            authorization = "Client-ID $accessKey",
                            query = searchQuery,
                            page = page,
                            perPage = 15
                        )
                        Log.d(TAG, "Unsplash: loaded ${response.results.size} wallpapers")
                        response.results.map { it.toWallpaper() }
                    } catch (e: Exception) {
                        Log.e(TAG, "Unsplash: failed - ${e.message}")
                        emptyList()
                    }
                }

                listOf(
                    pexelsDeferred.await(),
                    unsplashDeferred.await()
                )
            }

            // Round-robin interleave results from all sources
            val merged = mutableListOf<Wallpaper>()
            val maxSize = sources.maxOfOrNull { it.size } ?: 0
            for (i in 0 until maxSize) {
                for (source in sources) {
                    if (i < source.size) merged.add(source[i])
                }
            }

            Log.d(TAG, "Merged total: ${merged.size} wallpapers (page $page)")

            LoadResult.Page(
                data = merged,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (merged.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "Merged load failed: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}
