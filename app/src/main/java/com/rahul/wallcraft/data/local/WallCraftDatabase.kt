package com.rahul.wallcraft.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahul.wallcraft.data.local.dao.AiGenerationDao
import com.rahul.wallcraft.data.local.dao.FavoriteDao
import com.rahul.wallcraft.data.local.entity.AiGenerationEntity
import com.rahul.wallcraft.data.local.entity.AiQuotaEntity
import com.rahul.wallcraft.data.local.entity.FavoriteEntity

@Database(
    entities = [
        FavoriteEntity::class,
        AiGenerationEntity::class,
        AiQuotaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WallCraftDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun aiGenerationDao(): AiGenerationDao
}
