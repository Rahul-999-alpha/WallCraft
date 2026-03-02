package com.rahul.clearwalls.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.model.WallpaperSource

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    onClick: () -> Unit,
    onFavoriteClick: ((Wallpaper) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val aspectRatio = if (wallpaper.width > 0 && wallpaper.height > 0) {
        wallpaper.width.toFloat() / wallpaper.height.toFloat()
    } else {
        9f / 16f
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            SubcomposeAsyncImage(
                model = wallpaper.previewUrl,
                contentDescription = wallpaper.title,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "Failed to load",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio.coerceIn(0.4f, 1f))
            )

            // Source badge
            val sourceName = try {
                WallpaperSource.entries.find { wallpaper.id.startsWith(it.prefix + "_") }?.displayName
            } catch (_: Exception) { null }

            if (sourceName != null) {
                Text(
                    text = sourceName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Favorite button
            if (onFavoriteClick != null) {
                IconButton(
                    onClick = { onFavoriteClick(wallpaper) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(28.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (wallpaper.isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (wallpaper.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
