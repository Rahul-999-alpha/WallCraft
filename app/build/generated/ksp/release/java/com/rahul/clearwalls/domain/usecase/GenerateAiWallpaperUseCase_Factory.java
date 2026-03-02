package com.rahul.clearwalls.domain.usecase;

import com.rahul.clearwalls.domain.repository.AiGenerationRepository;
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
public final class GenerateAiWallpaperUseCase_Factory implements Factory<GenerateAiWallpaperUseCase> {
  private final Provider<AiGenerationRepository> repositoryProvider;

  public GenerateAiWallpaperUseCase_Factory(Provider<AiGenerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GenerateAiWallpaperUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GenerateAiWallpaperUseCase_Factory create(
      javax.inject.Provider<AiGenerationRepository> repositoryProvider) {
    return new GenerateAiWallpaperUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GenerateAiWallpaperUseCase_Factory create(
      Provider<AiGenerationRepository> repositoryProvider) {
    return new GenerateAiWallpaperUseCase_Factory(repositoryProvider);
  }

  public static GenerateAiWallpaperUseCase newInstance(AiGenerationRepository repository) {
    return new GenerateAiWallpaperUseCase(repository);
  }
}
