package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.UnsplashApi
import com.rahul.clearwalls.domain.model.Wallpaper

class UnsplashPagingSource(
    private val api: UnsplashApi,
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
            val accessKey = BuildConfig.UNSPLASH_ACCESS_KEY
            if (!MergedWallpaperPagingSource.isValidApiKey(accessKey)) {
                Log.w("UnsplashPaging", "Skipped: invalid/placeholder API key")
                return LoadResult.Page(emptyList(), null, null)
            }

            val response = api.searchPhotos(
                authorization = "Client-ID $accessKey",
                query = query,
                page = page,
                perPage = params.loadSize.coerceAtMost(30)
            )
            val wallpapers = response.results.map { it.toWallpaper() }
            Log.d("UnsplashPaging", "Loaded ${wallpapers.size} wallpapers (page $page)")
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.totalPages || wallpapers.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("UnsplashPaging", "Failed: ${e.message}")
            LoadResult.Error(e)
        }
    }
}
