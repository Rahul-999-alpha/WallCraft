package com.rahul.clearwalls.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahul.clearwalls.data.local.dao.AiGenerationDao
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao
import com.rahul.clearwalls.data.local.dao.FavoriteDao
import com.rahul.clearwalls.data.local.entity.AiGenerationEntity
import com.rahul.clearwalls.data.local.entity.AiQuotaEntity
import com.rahul.clearwalls.data.local.entity.CachedWallpaperEntity
import com.rahul.clearwalls.data.local.entity.FavoriteEntity

@Database(
    entities = [
        FavoriteEntity::class,
        AiGenerationEntity::class,
        AiQuotaEntity::class,
        CachedWallpaperEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ClearWallsDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun aiGenerationDao(): AiGenerationDao
    abstract fun cachedWallpaperDao(): CachedWallpaperDao
}
