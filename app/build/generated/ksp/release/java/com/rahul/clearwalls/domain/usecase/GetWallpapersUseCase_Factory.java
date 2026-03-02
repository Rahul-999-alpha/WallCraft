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
public final class GetWallpapersUseCase_Factory implements Factory<GetWallpapersUseCase> {
  private final Provider<WallpaperRepository> repositoryProvider;

  public GetWallpapersUseCase_Factory(Provider<WallpaperRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetWallpapersUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetWallpapersUseCase_Factory create(
      javax.inject.Provider<WallpaperRepository> repositoryProvider) {
    return new GetWallpapersUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetWallpapersUseCase_Factory create(
      Provider<WallpaperRepository> repositoryProvider) {
    return new GetWallpapersUseCase_Factory(repositoryProvider);
  }

  public static GetWallpapersUseCase newInstance(WallpaperRepository repository) {
    return new GetWallpapersUseCase(repository);
  }
}
