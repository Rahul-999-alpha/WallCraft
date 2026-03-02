package com.rahul.clearwalls.domain.usecase;

import com.rahul.clearwalls.domain.repository.FavoriteRepository;
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
public final class ToggleFavoriteUseCase_Factory implements Factory<ToggleFavoriteUseCase> {
  private final Provider<FavoriteRepository> repositoryProvider;

  public ToggleFavoriteUseCase_Factory(Provider<FavoriteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ToggleFavoriteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ToggleFavoriteUseCase_Factory create(
      javax.inject.Provider<FavoriteRepository> repositoryProvider) {
    return new ToggleFavoriteUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static ToggleFavoriteUseCase_Factory create(
      Provider<FavoriteRepository> repositoryProvider) {
    return new ToggleFavoriteUseCase_Factory(repositoryProvider);
  }

  public static ToggleFavoriteUseCase newInstance(FavoriteRepository repository) {
    return new ToggleFavoriteUseCase(repository);
  }
}
