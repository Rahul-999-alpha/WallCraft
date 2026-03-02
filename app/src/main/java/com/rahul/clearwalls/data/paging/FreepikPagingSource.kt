package com.rahul.clearwalls.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.FreepikApi
import com.rahul.clearwalls.domain.model.Wallpaper

class FreepikPagingSource(
    private val api: FreepikApi,
    private val query: String = "wallpaper"
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
            val apiKey = BuildConfig.FREEPIK_API_KEY
            if (apiKey.isBlank()) return LoadResult.Page(emptyList(), null, null)

            val response = api.searchResources(
                apiKey = apiKey,
                query = query,
                page = page,
                perPage = params.loadSize.coerceAtMost(50)
            )
            val wallpapers = response.data.mapNotNull { it.toWallpaper() }
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.meta != null && page >= response.meta.lastPage) null
                    else if (wallpapers.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
