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
public final class GetEditorPicksUseCase_Factory implements Factory<GetEditorPicksUseCase> {
  private final Provider<WallpaperRepository> repositoryProvider;

  public GetEditorPicksUseCase_Factory(Provider<WallpaperRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetEditorPicksUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetEditorPicksUseCase_Factory create(
      javax.inject.Provider<WallpaperRepository> repositoryProvider) {
    return new GetEditorPicksUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetEditorPicksUseCase_Factory create(
      Provider<WallpaperRepository> repositoryProvider) {
    return new GetEditorPicksUseCase_Factory(repositoryProvider);
  }

  public static GetEditorPicksUseCase newInstance(WallpaperRepository repository) {
    return new GetEditorPicksUseCase(repository);
  }
}
