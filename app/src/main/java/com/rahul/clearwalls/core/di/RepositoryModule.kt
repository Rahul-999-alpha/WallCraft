package com.rahul.clearwalls.core.di

import com.rahul.clearwalls.data.repository.AiGenerationRepositoryImpl
import com.rahul.clearwalls.data.repository.FavoriteRepositoryImpl
import com.rahul.clearwalls.data.repository.WallpaperRepositoryImpl
import com.rahul.clearwalls.domain.repository.AiGenerationRepository
import com.rahul.clearwalls.domain.repository.FavoriteRepository
import com.rahul.clearwalls.domain.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        impl: WallpaperRepositoryImpl
    ): WallpaperRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindAiGenerationRepository(
        impl: AiGenerationRepositoryImpl
    ): AiGenerationRepository
}
