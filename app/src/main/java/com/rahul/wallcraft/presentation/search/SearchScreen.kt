package com.rahul.wallcraft.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.presentation.components.WallCraftSearchBar
import com.rahul.wallcraft.presentation.components.WallpaperGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onWallpaperClick: (Wallpaper) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            WallCraftSearchBar(
                query = query,
                onQueryChange = { viewModel.onQueryChange(it) },
                onSearch = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            WallpaperGrid(
                wallpapers = searchResults,
                onWallpaperClick = onWallpaperClick,
                onFavoriteClick = { viewModel.toggleFavorite(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
