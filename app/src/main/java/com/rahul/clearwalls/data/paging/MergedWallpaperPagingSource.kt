package com.rahul.clearwalls.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.FreepikApi
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.data.remote.api.PixabayApi
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.data.remote.api.WallhavenApi
import com.rahul.clearwalls.domain.model.Wallpaper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MergedWallpaperPagingSource(
    private val pixabayApi: PixabayApi,
    private val wallhavenApi: WallhavenApi,
    private val pexelsApi: PexelsApi?,
    private val unsplashApi: UnsplashApi?,
    private val freepikApi: FreepikApi?,
    private val query: String = "",
    private val category: String? = null
) : PagingSource<Int, Wallpaper>() {

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
                val pixabayDeferred = async {
                    try {
                        val response = pixabayApi.searchImages(
                            apiKey = BuildConfig.PIXABAY_API_KEY,
                            query = searchQuery,
                            page = page,
                            perPage = 8,
                            category = category
                        )
                        response.hits.map { it.toWallpaper() }
                    } catch (_: Exception) { emptyList() }
                }

                val wallhavenDeferred = async {
                    try {
                        val response = wallhavenApi.searchWallpapers(
                            apiKey = BuildConfig.WALLHAVEN_API_KEY,
                            query = searchQuery,
                            page = page
                        )
                        response.data.map { it.toWallpaper() }.take(8)
                    } catch (_: Exception) { emptyList() }
                }

                val pexelsDeferred = async {
                    try {
                        val apiKey = BuildConfig.PEXELS_API_KEY
                        if (apiKey.isBlank() || pexelsApi == null) return@async emptyList()
                        val response = pexelsApi.searchPhotos(
                            apiKey = apiKey,
                            query = searchQuery,
                            page = page,
                            perPage = 8
                        )
                        response.photos.map { it.toWallpaper() }
                    } catch (_: Exception) { emptyList() }
                }

                val unsplashDeferred = async {
                    try {
                        val accessKey = BuildConfig.UNSPLASH_ACCESS_KEY
                        if (accessKey.isBlank() || unsplashApi == null) return@async emptyList()
                        val response = unsplashApi.searchPhotos(
                            authorization = "Client-ID $accessKey",
                            query = searchQuery,
                            page = page,
                            perPage = 8
                        )
                        response.results.map { it.toWallpaper() }
                    } catch (_: Exception) { emptyList() }
                }

                val freepikDeferred = async {
                    try {
                        val apiKey = BuildConfig.FREEPIK_API_KEY
                        if (apiKey.isBlank() || freepikApi == null) return@async emptyList()
                        val response = freepikApi.searchResources(
                            apiKey = apiKey,
                            query = searchQuery,
                            page = page,
                            perPage = 8
                        )
                        response.data.mapNotNull { it.toWallpaper() }
                    } catch (_: Exception) { emptyList() }
                }

                listOf(
                    pixabayDeferred.await(),
                    wallhavenDeferred.await(),
                    pexelsDeferred.await(),
                    unsplashDeferred.await(),
                    freepikDeferred.await()
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

            LoadResult.Page(
                data = merged,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (merged.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
