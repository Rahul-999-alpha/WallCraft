package com.rahul.wallcraft.core.di

import android.content.Context
import androidx.room.Room
import com.rahul.wallcraft.core.common.Constants
import com.rahul.wallcraft.data.local.WallCraftDatabase
import com.rahul.wallcraft.data.local.dao.AiGenerationDao
import com.rahul.wallcraft.data.local.dao.FavoriteDao
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
    fun provideDatabase(@ApplicationContext context: Context): WallCraftDatabase =
        Room.databaseBuilder(
            context,
            WallCraftDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideFavoriteDao(database: WallCraftDatabase): FavoriteDao =
        database.favoriteDao()

    @Provides
    fun provideAiGenerationDao(database: WallCraftDatabase): AiGenerationDao =
        database.aiGenerationDao()
}
