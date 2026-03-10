package com.rahul.clearwalls.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.presentation.components.AdBanner
import com.rahul.clearwalls.presentation.components.NativeAdCard
import com.rahul.clearwalls.presentation.components.SectionHeader
import com.rahul.clearwalls.presentation.components.WallpaperCard
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onWallpaperClick: (Wallpaper) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose {}
    }

    val wallpapers = viewModel.wallpapers.collectAsLazyPagingItems()
    val editorPicks = viewModel.editorPicks.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var isRefreshing by remember { mutableStateOf(false) }

    if (wallpapers.loadState.refresh !is LoadState.Loading) {
        isRefreshing = false
    }

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = greeting,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "ClearWalls",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { AdBanner() },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        // Wallpaper grid item counts (for ad interleaving)
        val adInterval = Constants.AD_INLINE_INTERVAL
        val wpCount = wallpapers.itemCount
        val numAds = if (wpCount > adInterval) wpCount / adInterval else 0
        val totalWpItems = wpCount + numAds

        // Header items count: Editor's Picks header + row + spacer + For You header
        val hasEditorPicks = editorPicks.itemCount > 0
        val headerCount = if (hasEditorPicks) 3 else 1 // (EP header, EP row, For You header) or just (For You header)

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                wallpapers.refresh()
                editorPicks.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = headerCount + totalWpItems,
                    span = { index ->
                        if (index < headerCount) {
                            StaggeredGridItemSpan.FullLine
                        } else {
                            val wpGridIndex = index - headerCount
                            if (isNativeAdPosition(wpGridIndex, adInterval)) {
                                StaggeredGridItemSpan.FullLine
                            } else {
                                StaggeredGridItemSpan.SingleLane
                            }
                        }
                    }
                ) { index ->
                    when {
                        // Editor's Picks section header
                        hasEditorPicks && index == 0 -> {
                            SectionHeader(title = "Editor's Picks")
                        }
                        // Editor's Picks horizontal row
                        hasEditorPicks && index == 1 -> {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(minOf(editorPicks.itemCount, 10)) { epIndex ->
                                    editorPicks[epIndex]?.let { wallpaper ->
                                        WallpaperCard(
                                            wallpaper = wallpaper,
                                            onClick = { onWallpaperClick(wallpaper) },
                                            onFavoriteClick = { viewModel.toggleFavorite(it) },
                                            modifier = Modifier.width(160.dp)
                                        )
                                    }
                                }
                            }
                        }
                        // "For You" section header
                        index == headerCount - 1 -> {
                            SectionHeader(title = "For You")
                        }
                        // Wallpaper grid items + native ads
                        else -> {
                            val wpGridIndex = index - headerCount
                            if (isNativeAdPosition(wpGridIndex, adInterval)) {
                                NativeAdCard(
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            } else {
                                val wpIndex = toWallpaperIndex(wpGridIndex, adInterval)
                                if (wpIndex < wpCount) {
                                    wallpapers[wpIndex]?.let { wallpaper ->
                                        WallpaperCard(
                                            wallpaper = wallpaper,
                                            onClick = { onWallpaperClick(wallpaper) },
                                            onFavoriteClick = { viewModel.toggleFavorite(it) }
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
