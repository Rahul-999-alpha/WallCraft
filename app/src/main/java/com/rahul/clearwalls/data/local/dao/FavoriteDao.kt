package com.rahul.clearwalls.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahul.clearwalls.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE wallpaperId = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorites WHERE wallpaperId = :id LIMIT 1")
    suspend fun getById(id: String): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE wallpaperId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getCount(): Int
}
