package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.domain.model.Wallpaper
import kotlinx.coroutines.CancellationException
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

            // Each source returns Result: success (possibly empty when its key is absent)
            // or failure. We keep per-source isolation, but no longer collapse a real
            // network/quota failure into a fake empty page. CancellationException is
            // rethrown so structured concurrency is preserved.
            val results = coroutineScope {
                // DISABLED — no API keys. Uncomment when keys are obtained.
                // val pixabayDeferred = async { ... }

                val pexelsDeferred = async {
                    val apiKey = BuildConfig.PEXELS_API_KEY
                    if (!isValidApiKey(apiKey)) {
                        Log.w(TAG, "Pexels: skipped (invalid key)")
                        Result.success(emptyList())
                    } else {
                        try {
                            val response = pexelsApi.searchPhotos(
                                apiKey = apiKey,
                                query = searchQuery,
                                page = page,
                                perPage = 15
                            )
                            Log.d(TAG, "Pexels: loaded ${response.photos.size} wallpapers")
                            Result.success(response.photos.map { it.toWallpaper() })
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Log.e(TAG, "Pexels: failed - ${e.message}")
                            Result.failure(e)
                        }
                    }
                }

                val unsplashDeferred = async {
                    val accessKey = BuildConfig.UNSPLASH_ACCESS_KEY
                    if (!isValidApiKey(accessKey)) {
                        Log.w(TAG, "Unsplash: skipped (invalid key)")
                        Result.success(emptyList())
                    } else {
                        try {
                            val response = unsplashApi.searchPhotos(
                                authorization = "Client-ID $accessKey",
                                query = searchQuery,
                                page = page,
                                perPage = 15
                            )
                            Log.d(TAG, "Unsplash: loaded ${response.results.size} wallpapers")
                            Result.success(response.results.map { it.toWallpaper() })
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Log.e(TAG, "Unsplash: failed - ${e.message}")
                            Result.failure(e)
                        }
                    }
                }

                listOf(
                    pexelsDeferred.await(),
                    unsplashDeferred.await()
                )
            }

            val sources = results.mapNotNull { it.getOrNull() }
            val hasData = sources.any { it.isNotEmpty() }

            // If we gathered no data and at least one source actually errored, surface the
            // error so the UI can show a retry instead of a fake "no wallpapers" empty
            // state. This also un-hides Pexels/Unsplash key-quota exhaustion in production.
            if (!hasData && results.any { it.isFailure }) {
                val error = results.firstNotNullOfOrNull { it.exceptionOrNull() }
                    ?: Exception("Failed to load wallpapers")
                Log.e(TAG, "All sources failed with no data: ${error.message}", error)
                return LoadResult.Error(error)
            }

            // Round-robin interleave results from all sources
            val merged = mutableListOf<Wallpaper>()
            val maxSize = sources.maxOfOrNull { it.size } ?: 0
            for (i in 0 until maxSize) {
                for (source in sources) {
                    if (i < source.size) merged.add(source[i])
                }
            }

            merged.shuffle()
            Log.d(TAG, "Merged total: ${merged.size} wallpapers (page $page, shuffled)")

            LoadResult.Page(
                data = merged,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (merged.isEmpty()) null else page + 1
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Merged load failed: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}
