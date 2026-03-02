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
public final class GetAiQuotaUseCase_Factory implements Factory<GetAiQuotaUseCase> {
  private final Provider<AiGenerationRepository> repositoryProvider;

  public GetAiQuotaUseCase_Factory(Provider<AiGenerationRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAiQuotaUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAiQuotaUseCase_Factory create(
      javax.inject.Provider<AiGenerationRepository> repositoryProvider) {
    return new GetAiQuotaUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetAiQuotaUseCase_Factory create(
      Provider<AiGenerationRepository> repositoryProvider) {
    return new GetAiQuotaUseCase_Factory(repositoryProvider);
  }

  public static GetAiQuotaUseCase newInstance(AiGenerationRepository repository) {
    return new GetAiQuotaUseCase(repository);
  }
}
