package com.rahul.clearwalls.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PinterestApi
import com.rahul.clearwalls.domain.model.Wallpaper

class PinterestPagingSource(
    private val api: PinterestApi,
    private val query: String = "wallpaper"
) : PagingSource<String, Wallpaper>() {

    override fun getRefreshKey(state: PagingState<String, Wallpaper>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Wallpaper> {
        return try {
            val token = BuildConfig.PINTEREST_ACCESS_TOKEN
            if (token.isBlank()) return LoadResult.Page(emptyList(), null, null)

            val response = api.searchPins(
                authorization = "Bearer $token",
                query = query,
                bookmark = params.key
            )
            val wallpapers = response.items.mapNotNull { it.toWallpaper() }
            LoadResult.Page(
                data = wallpapers,
                prevKey = null,
                nextKey = response.bookmark
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
