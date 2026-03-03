package com.rahul.clearwalls.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity

@Dao
interface CachedWallpaperDao {
    @Query("SELECT * FROM cached_wallpapers ORDER BY cachedAt DESC")
    suspend fun getAllCached(): List<CachedWallpaperEntity>

    @Query("SELECT * FROM cached_wallpapers WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CachedWallpaperEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wallpapers: List<CachedWallpaperEntity>)

    @Query("DELETE FROM cached_wallpapers WHERE cachedAt < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)

    @Query("SELECT COUNT(*) FROM cached_wallpapers")
    suspend fun getCount(): Int
}
