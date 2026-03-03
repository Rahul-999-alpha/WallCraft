package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.data.remote.api.PixabayApi
import com.rahul.clearwalls.domain.model.Wallpaper

class PixabayPagingSource(
    private val api: PixabayApi,
    private val query: String = "",
    private val category: String? = null,
    private val editorsChoice: Boolean = false
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
            val apiKey = BuildConfig.PIXABAY_API_KEY
            if (!MergedWallpaperPagingSource.isValidApiKey(apiKey)) {
                Log.w("PixabayPaging", "Skipped: invalid/placeholder API key")
                return LoadResult.Page(emptyList(), null, null)
            }
            val response = api.searchImages(
                apiKey = apiKey,
                query = query,
                page = page,
                perPage = params.loadSize.coerceAtMost(200),
                category = category,
                editorsChoice = editorsChoice
            )
            val wallpapers = response.hits.map { it.toWallpaper() }
            Log.d("PixabayPaging", "Loaded ${wallpapers.size} wallpapers (page $page)")
            LoadResult.Page(
                data = wallpapers,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (wallpapers.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PixabayPaging", "Failed: ${e.message}")
            LoadResult.Error(e)
        }
    }
}
