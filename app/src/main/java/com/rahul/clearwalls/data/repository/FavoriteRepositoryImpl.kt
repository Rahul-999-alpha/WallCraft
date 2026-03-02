package com.rahul.clearwalls.data.repository

import com.rahul.clearwalls.data.local.dao.FavoriteDao
import com.rahul.clearwalls.data.mapper.toFavoriteEntity
import com.rahul.clearwalls.data.mapper.toWallpaper
import com.rahul.clearwalls.domain.model.Wallpaper
import com.rahul.clearwalls.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<Wallpaper>> =
        favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toWallpaper() }
        }

    override suspend fun toggleFavorite(wallpaper: Wallpaper): Boolean {
        val isFav = favoriteDao.isFavorite(wallpaper.id)
        if (isFav) {
            favoriteDao.deleteById(wallpaper.id)
        } else {
            favoriteDao.insert(wallpaper.toFavoriteEntity())
        }
        return !isFav
    }

    override suspend fun isFavorite(wallpaperId: String): Boolean =
        favoriteDao.isFavorite(wallpaperId)

    override suspend fun removeFavorite(wallpaperId: String) {
        favoriteDao.deleteById(wallpaperId)
    }
}
