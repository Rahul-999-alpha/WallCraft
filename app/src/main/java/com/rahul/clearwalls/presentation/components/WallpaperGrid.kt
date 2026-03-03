package com.rahul.clearwalls.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.domain.model.Wallpaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperGrid(
    wallpapers: LazyPagingItems<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    onFavoriteClick: ((Wallpaper) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }

    if (wallpapers.loadState.refresh !is LoadState.Loading) {
        isRefreshing = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (wallpapers.loadState.refresh) {
            is LoadState.Loading -> {
                if (!isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is LoadState.Error -> {
                val error = (wallpapers.loadState.refresh as LoadState.Error).error
                Text(
                    text = error.localizedMessage ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {
                if (wallpapers.itemCount == 0) {
                    EmptyState(
                        icon = Icons.Outlined.SearchOff,
                        title = "No wallpapers found",
                        description = "Try adjusting your filters or search query",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            isRefreshing = true
                            wallpapers.refresh()
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalItemSpacing = 8.dp,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val adInterval = Constants.AD_INLINE_INTERVAL
                            val wpCount = wallpapers.itemCount
                            val numAds = if (wpCount > adInterval) wpCount / adInterval else 0
                            val totalItems = wpCount + numAds

                            items(
                                count = totalItems,
                                span = { index ->
                                    if (isNativeAdPosition(index, adInterval)) {
                                        StaggeredGridItemSpan.FullLine
                                    } else {
                                        StaggeredGridItemSpan.SingleLane
                                    }
                                }
                            ) { index ->
                                if (isNativeAdPosition(index, adInterval)) {
                                    NativeAdCard(
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                } else {
                                    val wpIndex = toWallpaperIndex(index, adInterval)
                                    if (wpIndex < wpCount) {
                                        wallpapers[wpIndex]?.let { wallpaper ->
                                            WallpaperCard(
                                                wallpaper = wallpaper,
                                                onClick = { onWallpaperClick(wallpaper) },
                                                onFavoriteClick = onFavoriteClick
                                            )
                                        }
                                    }
                                }
                            }

                            // Loading more indicator
                            if (wallpapers.loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.padding(16.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isNativeAdPosition(index: Int, adInterval: Int): Boolean {
    if (index < adInterval) return false
    return (index - adInterval) % (adInterval + 1) == 0
}

private fun toWallpaperIndex(index: Int, adInterval: Int): Int {
    if (index < adInterval) return index
    val adsShown = (index - adInterval) / (adInterval + 1) + 1
    return index - adsShown
}
