package com.rahul.clearwalls.data.paging

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
            val response = api.searchWallpapers(
                apiKey = BuildConfig.WALLHAVEN_API_KEY,
                query = query,
                page = page
            )
            val wallpapers = response.data.map { it.toWallpaper() }
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.meta.lastPage) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
