package com.rahul.clearwalls.presentation.search;

import com.rahul.clearwalls.domain.usecase.SearchWallpapersUseCase;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<SearchWallpapersUseCase> searchWallpapersUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  public SearchViewModel_Factory(Provider<SearchWallpapersUseCase> searchWallpapersUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    this.searchWallpapersUseCaseProvider = searchWallpapersUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(searchWallpapersUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get());
  }

  public static SearchViewModel_Factory create(
      javax.inject.Provider<SearchWallpapersUseCase> searchWallpapersUseCaseProvider,
      javax.inject.Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new SearchViewModel_Factory(Providers.asDaggerProvider(searchWallpapersUseCaseProvider), Providers.asDaggerProvider(toggleFavoriteUseCaseProvider));
  }

  public static SearchViewModel_Factory create(
      Provider<SearchWallpapersUseCase> searchWallpapersUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new SearchViewModel_Factory(searchWallpapersUseCaseProvider, toggleFavoriteUseCaseProvider);
  }

  public static SearchViewModel newInstance(SearchWallpapersUseCase searchWallpapersUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase) {
    return new SearchViewModel(searchWallpapersUseCase, toggleFavoriteUseCase);
  }
}
