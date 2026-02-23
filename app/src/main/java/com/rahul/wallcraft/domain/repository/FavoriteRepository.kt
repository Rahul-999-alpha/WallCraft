package com.rahul.wallcraft.domain.repository

import com.rahul.wallcraft.domain.model.Wallpaper
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Wallpaper>>
    suspend fun toggleFavorite(wallpaper: Wallpaper): Boolean
    suspend fun isFavorite(wallpaperId: String): Boolean
    suspend fun removeFavorite(wallpaperId: String)
}
