package com.rahul.clearwalls.presentation.browse;

import androidx.lifecycle.SavedStateHandle;
import com.rahul.clearwalls.domain.usecase.GetCategoriesUseCase;
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
public final class BrowseViewModel_Factory implements Factory<BrowseViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  public BrowseViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getWallpapersUseCaseProvider = getWallpapersUseCaseProvider;
    this.getCategoriesUseCaseProvider = getCategoriesUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
  }

  @Override
  public BrowseViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getWallpapersUseCaseProvider.get(), getCategoriesUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get());
  }

  public static BrowseViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      javax.inject.Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      javax.inject.Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new BrowseViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(getWallpapersUseCaseProvider), Providers.asDaggerProvider(getCategoriesUseCaseProvider), Providers.asDaggerProvider(toggleFavoriteUseCaseProvider));
  }

  public static BrowseViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetWallpapersUseCase> getWallpapersUseCaseProvider,
      Provider<GetCategoriesUseCase> getCategoriesUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider) {
    return new BrowseViewModel_Factory(savedStateHandleProvider, getWallpapersUseCaseProvider, getCategoriesUseCaseProvider, toggleFavoriteUseCaseProvider);
  }

  public static BrowseViewModel newInstance(SavedStateHandle savedStateHandle,
      GetWallpapersUseCase getWallpapersUseCase, GetCategoriesUseCase getCategoriesUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase) {
    return new BrowseViewModel(savedStateHandle, getWallpapersUseCase, getCategoriesUseCase, toggleFavoriteUseCase);
  }
}
