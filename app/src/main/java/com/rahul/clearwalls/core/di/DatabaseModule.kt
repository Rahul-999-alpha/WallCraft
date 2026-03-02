package com.rahul.clearwalls.core.di

import android.content.Context
import androidx.room.Room
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.data.local.ClearWallsDatabase
import com.rahul.clearwalls.data.local.dao.AiGenerationDao
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao
import com.rahul.clearwalls.data.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ClearWallsDatabase =
        Room.databaseBuilder(
            context,
            ClearWallsDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideFavoriteDao(database: ClearWallsDatabase): FavoriteDao =
        database.favoriteDao()

    @Provides
    fun provideAiGenerationDao(database: ClearWallsDatabase): AiGenerationDao =
        database.aiGenerationDao()

    @Provides
    fun provideCachedWallpaperDao(database: ClearWallsDatabase): CachedWallpaperDao =
        database.cachedWallpaperDao()
}
