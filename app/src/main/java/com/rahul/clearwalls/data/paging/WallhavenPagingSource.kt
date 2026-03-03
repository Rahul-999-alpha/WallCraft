package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.WallhavenApi
import com.rahul.clearwalls.domain.model.Wallpaper

class WallhavenPagingSource(
    private val api: WallhavenApi,
    private val query: String = ""
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
            val apiKey = BuildConfig.WALLHAVEN_API_KEY
            if (!MergedWallpaperPagingSource.isValidApiKey(apiKey)) {
                Log.w("WallhavenPaging", "Skipped: invalid/placeholder API key")
                return LoadResult.Page(emptyList(), null, null)
            }
            val response = api.searchWallpapers(
                apiKey = apiKey,
                query = query,
                page = page
            )
            val wallpapers = response.data.map { it.toWallpaper() }
            Log.d("WallhavenPaging", "Loaded ${wallpapers.size} wallpapers (page $page)")
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.meta.lastPage) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("WallhavenPaging", "Failed: ${e.message}")
            LoadResult.Error(e)
        }
    }
}
