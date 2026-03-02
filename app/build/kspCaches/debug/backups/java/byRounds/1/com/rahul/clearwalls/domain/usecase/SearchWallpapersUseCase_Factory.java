package com.rahul.clearwalls.domain.usecase;

import com.rahul.clearwalls.domain.repository.WallpaperRepository;
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
public final class SearchWallpapersUseCase_Factory implements Factory<SearchWallpapersUseCase> {
  private final Provider<WallpaperRepository> repositoryProvider;

  public SearchWallpapersUseCase_Factory(Provider<WallpaperRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchWallpapersUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchWallpapersUseCase_Factory create(
      javax.inject.Provider<WallpaperRepository> repositoryProvider) {
    return new SearchWallpapersUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static SearchWallpapersUseCase_Factory create(
      Provider<WallpaperRepository> repositoryProvider) {
    return new SearchWallpapersUseCase_Factory(repositoryProvider);
  }

  public static SearchWallpapersUseCase newInstance(WallpaperRepository repository) {
    return new SearchWallpapersUseCase(repository);
  }
}
