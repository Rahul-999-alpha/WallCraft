package com.rahul.clearwalls.presentation.detail;

import androidx.lifecycle.SavedStateHandle;
import com.rahul.clearwalls.domain.repository.FavoriteRepository;
import com.rahul.clearwalls.domain.repository.WallpaperRepository;
import com.rahul.clearwalls.domain.usecase.DownloadWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.SetWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class WallpaperDetailViewModel_Factory implements Factory<WallpaperDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<WallpaperRepository> wallpaperRepositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<SetWallpaperUseCase> setWallpaperUseCaseProvider;

  private final Provider<DownloadWallpaperUseCase> downloadWallpaperUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  public WallpaperDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WallpaperRepository> wallpaperRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<SetWallpaperUseCase> setWallpaperUseCaseProvider,
      Provider<DownloadWallpaperUseCase> downloadWallpaperUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.wallpaperRepositoryProvider = wallpaperRepositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.setWallpaperUseCaseProvider = setWallpaperUseCaseProvider;
    this.downloadWallpaperUseCaseProvider = downloadWallpaperUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
  }

  @Override
  public WallpaperDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), wallpaperRepositoryProvider.get(), favoriteRepositoryProvider.get(), setWallpaperUseCaseProvider.get(), downloadWallpaperUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get());
  }

  public static WallpaperDetailViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<WallpaperRepository> wallpaperRepositoryProvider,
      javax.inject.Provider<FavoriteRepository> favoriteRepositoryProvider,
      javax.inject.Provider<SetWallpaperUseCase> setWallpaperUseCaseProvider,
      javax.inject.Provider<DownloadWallpaperUseCase> downloadWallpaperUseCaseProvider,
      javax.inject.Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new WallpaperDetailViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(wallpaperRepositoryProvider), Providers.asDaggerProvider(favoriteRepositoryProvider), Providers.asDaggerProvider(setWallpaperUseCaseProvider), Providers.asDaggerProvider(downloadWallpaperUseCaseProvider), Providers.asDaggerProvider(toggleFavoriteUseCaseProvider));
  }

  public static WallpaperDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<WallpaperRepository> wallpaperRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<SetWallpaperUseCase> setWallpaperUseCaseProvider,
      Provider<DownloadWallpaperUseCase> downloadWallpaperUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new WallpaperDetailViewModel_Factory(savedStateHandleProvider, wallpaperRepositoryProvider, favoriteRepositoryProvider, setWallpaperUseCaseProvider, downloadWallpaperUseCaseProvider, toggleFavoriteUseCaseProvider);
  }

  public static WallpaperDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      WallpaperRepository wallpaperRepository, FavoriteRepository favoriteRepository,
      SetWallpaperUseCase setWallpaperUseCase, DownloadWallpaperUseCase downloadWallpaperUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase) {
    return new WallpaperDetailViewModel(savedStateHandle, wallpaperRepository, favoriteRepository, setWallpaperUseCase, downloadWallpaperUseCase, toggleFavoriteUseCase);
  }
}
