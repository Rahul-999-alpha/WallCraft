package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PexelsApi
import com.rahul.clearwalls.domain.model.Wallpaper

class PexelsPagingSource(
    private val api: PexelsApi,
    private val query: String = "wallpaper",
    private val curated: Boolean = false
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
            val apiKey = BuildConfig.PEXELS_API_KEY
            if (!MergedWallpaperPagingSource.isValidApiKey(apiKey)) {
                Log.w("PexelsPaging", "Skipped: invalid/placeholder API key")
                return LoadResult.Page(emptyList(), null, null)
            }

            val response = if (curated) {
                api.getCurated(apiKey = apiKey, page = page, perPage = params.loadSize.coerceAtMost(80))
            } else {
                api.searchPhotos(apiKey = apiKey, query = query, page = page, perPage = params.loadSize.coerceAtMost(80))
            }
            val wallpapers = response.photos.map { it.toWallpaper() }
            Log.d("PexelsPaging", "Loaded ${wallpapers.size} wallpapers (page $page)")
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (wallpapers.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PexelsPaging", "Failed: ${e.message}")
            LoadResult.Error(e)
        }
    }
}
