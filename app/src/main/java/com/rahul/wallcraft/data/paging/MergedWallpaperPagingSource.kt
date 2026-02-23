package com.rahul.wallcraft.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rahul.wallcraft.BuildConfig
import com.rahul.wallcraft.data.mapper.toWallpaper
import com.rahul.wallcraft.data.remote.api.PixabayApi
import com.rahul.wallcraft.data.remote.api.WallhavenApi
import com.rahul.wallcraft.domain.model.Wallpaper

class MergedWallpaperPagingSource(
    private val pixabayApi: PixabayApi,
    private val wallhavenApi: WallhavenApi,
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
            val pixabayWallpapers = try {
                val response = pixabayApi.searchImages(
                    apiKey = BuildConfig.PIXABAY_API_KEY,
                    query = query,
                    page = page,
                    perPage = 10,
                    category = category
                )
                response.hits.map { it.toWallpaper() }
            } catch (e: Exception) {
                emptyList()
            }

            val wallhavenWallpapers = try {
                val response = wallhavenApi.searchWallpapers(
                    apiKey = BuildConfig.WALLHAVEN_API_KEY,
                    query = query,
                    page = page
                )
                response.data.map { it.toWallpaper() }.take(10)
            } catch (e: Exception) {
                emptyList()
            }

            // Interleave results: alternating between sources
            val merged = mutableListOf<Wallpaper>()
            val maxSize = maxOf(pixabayWallpapers.size, wallhavenWallpapers.size)
            for (i in 0 until maxSize) {
                if (i < pixabayWallpapers.size) merged.add(pixabayWallpapers[i])
                if (i < wallhavenWallpapers.size) merged.add(wallhavenWallpapers[i])
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
