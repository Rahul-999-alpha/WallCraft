package com.rahul.clearwalls.presentation.home;

import com.rahul.clearwalls.domain.repository.FavoriteRepository;
import com.rahul.clearwalls.domain.usecase.GetCategoriesUseCase;
import com.rahul.clearwalls.domain.usecase.GetEditorPicksUseCase;
import com.rahul.clearwalls.domain.usecase.GetWallpapersUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider;

  private final Provider<GetEditorPicksUseCase> getEditorPicksUseCaseProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  public HomeViewModel_Factory(Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      Provider<GetEditorPicksUseCase> getEditorPicksUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    this.getWallpapersUseCaseProvider = getWallpapersUseCaseProvider;
    this.getEditorPicksUseCaseProvider = getEditorPicksUseCaseProvider;
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getWallpapersUseCaseProvider.get(), getEditorPicksUseCaseProvider.get(), getCategoriesUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), favoriteRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      javax.inject.Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      javax.inject.Provider<GetEditorPicksUseCase> getEditorPicksUseCaseProvider,
      javax.inject.Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      javax.inject.Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      javax.inject.Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new HomeViewModel_Factory(Providers.asDaggerProvider(getWallpapersUseCaseProvider), Providers.asDaggerProvider(getEditorPicksUseCaseProvider), Providers.asDaggerProvider(getCategoriesUseCaseProvider), Providers.asDaggerProvider(toggleFavoriteUseCaseProvider), Providers.asDaggerProvider(favoriteRepositoryProvider));
  }

  public static HomeViewModel_Factory create(
      Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      Provider<GetEditorPicksUseCase> getEditorPicksUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new HomeViewModel_Factory(getWallpapersUseCaseProvider, getEditorPicksUseCaseProvider, getCategoriesUseCaseProvider, toggleFavoriteUseCaseProvider, favoriteRepositoryProvider);
  }

  public static HomeViewModel newInstance(GetWallpapersUseCase getWallpapersUseCase,
      GetEditorPicksUseCase getEditorPicksUseCase, GetCategoriesUseCase getCategoriesUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase, FavoriteRepository favoriteRepository) {
    return new HomeViewModel(getWallpapersUseCase, getEditorPicksUseCase, getCategoriesUseCase, toggleFavoriteUseCase, favoriteRepository);
  }
}
