package com.rahul.clearwalls.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.presentation.components.AdBanner
import com.rahul.clearwalls.presentation.components.CategoryChip
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
    val categories by viewModel.categories.collectAsState()
    val wallpapers = viewModel.wallpapers.collectAsLazyPagingItems()
    val editorPicks = viewModel.editorPicks.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Categories row
            if (categories.isNotEmpty()) {
                SectionHeader(title = "Categories")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            label = category.displayName,
                            selected = false,
                            onClick = { onCategoryClick(category.name) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Editor's Picks horizontal row
            if (editorPicks.itemCount > 0) {
                SectionHeader(title = "Editor's Picks")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(minOf(editorPicks.itemCount, 10)) { index ->
                        editorPicks[index]?.let { wallpaper ->
                            WallpaperCard(
                                wallpaper = wallpaper,
                                onClick = { onWallpaperClick(wallpaper) },
                                onFavoriteClick = { viewModel.toggleFavorite(it) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // For You section header
            SectionHeader(title = "For You")

            // Wallpaper grid
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                items(wallpapers.itemCount) { index ->
                    wallpapers[index]?.let { wallpaper ->
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
