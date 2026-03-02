package com.rahul.clearwalls.presentation.aigenerate;

import com.rahul.clearwalls.domain.repository.AiGenerationRepository;
import com.rahul.clearwalls.domain.usecase.GenerateAiWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.GetAiQuotaUseCase;
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
public final class AiGenerateViewModel_Factory implements Factory<AiGenerateViewModel> {
  private final Provider<GenerateAiWallpaperUseCase> generateAiWallpaperUseCaseProvider;

  private final Provider<GetAiQuotaUseCase> getAiQuotaUseCaseProvider;

  private final Provider<AiGenerationRepository> aiGenerationRepositoryProvider;

  public AiGenerateViewModel_Factory(
      Provider<GenerateAiWallpaperUseCase> generateAiWallpaperUseCaseProvider,
      Provider<GetAiQuotaUseCase> getAiQuotaUseCaseProvider,
      Provider<AiGenerationRepository> aiGenerationRepositoryProvider) {
    this.generateAiWallpaperUseCaseProvider = generateAiWallpaperUseCaseProvider;
    this.getAiQuotaUseCaseProvider = getAiQuotaUseCaseProvider;
    this.aiGenerationRepositoryProvider = aiGenerationRepositoryProvider;
  }

  @Override
  public AiGenerateViewModel get() {
    return newInstance(generateAiWallpaperUseCaseProvider.get(), getAiQuotaUseCaseProvider.get(), aiGenerationRepositoryProvider.get());
  }

  public static AiGenerateViewModel_Factory create(
      javax.inject.Provider<GenerateAiWallpaperUseCase> generateAiWallpaperUseCaseProvider,
      javax.inject.Provider<GetAiQuotaUseCase> getAiQuotaUseCaseProvider,
      javax.inject.Provider<AiGenerationRepository> aiGenerationRepositoryProvider) {
    return new AiGenerateViewModel_Factory(Providers.asDaggerProvider(generateAiWallpaperUseCaseProvider), Providers.asDaggerProvider(getAiQuotaUseCaseProvider), Providers.asDaggerProvider(aiGenerationRepositoryProvider));
  }

  public static AiGenerateViewModel_Factory create(
      Provider<GenerateAiWallpaperUseCase> generateAiWallpaperUseCaseProvider,
      Provider<GetAiQuotaUseCase> getAiQuotaUseCaseProvider,
      Provider<AiGenerationRepository> aiGenerationRepositoryProvider) {
    return new AiGenerateViewModel_Factory(generateAiWallpaperUseCaseProvider, getAiQuotaUseCaseProvider, aiGenerationRepositoryProvider);
  }

  public static AiGenerateViewModel newInstance(
      GenerateAiWallpaperUseCase generateAiWallpaperUseCase, GetAiQuotaUseCase getAiQuotaUseCase,
      AiGenerationRepository aiGenerationRepository) {
    return new AiGenerateViewModel(generateAiWallpaperUseCase, getAiQuotaUseCase, aiGenerationRepository);
  }
}
