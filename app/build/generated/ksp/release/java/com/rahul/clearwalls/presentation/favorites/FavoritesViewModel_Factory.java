package com.rahul.clearwalls.presentation.favorites;

import com.rahul.clearwalls.domain.repository.FavoriteRepository;
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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  public FavoritesViewModel_Factory(Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(favoriteRepositoryProvider.get(), toggleFavoriteUseCaseProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      javax.inject.Provider<FavoriteRepository> favoriteRepositoryProvider,
      javax.inject.Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new FavoritesViewModel_Factory(Providers.asDaggerProvider(favoriteRepositoryProvider), Providers.asDaggerProvider(toggleFavoriteUseCaseProvider));
  }

  public static FavoritesViewModel_Factory create(
      Provider<FavoriteRepository> favoriteRepositoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new FavoritesViewModel_Factory(favoriteRepositoryProvider, toggleFavoriteUseCaseProvider);
  }

  public static FavoritesViewModel newInstance(FavoriteRepository favoriteRepository,
      ToggleFavoriteUseCase toggleFavoriteUseCase) {
    return new FavoritesViewModel(favoriteRepository, toggleFavoriteUseCase);
  }
}
