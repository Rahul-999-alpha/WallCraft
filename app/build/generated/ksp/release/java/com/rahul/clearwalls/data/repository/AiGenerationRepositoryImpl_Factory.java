package com.rahul.clearwalls.data.repository;

import android.content.Context;
import com.rahul.clearwalls.data.local.dao.AiGenerationDao;
import com.rahul.clearwalls.data.remote.api.StabilityAiApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AiGenerationRepositoryImpl_Factory implements Factory<AiGenerationRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<StabilityAiApi> stabilityAiApiProvider;

  private final Provider<AiGenerationDao> aiGenerationDaoProvider;

  public AiGenerationRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<StabilityAiApi> stabilityAiApiProvider,
      Provider<AiGenerationDao> aiGenerationDaoProvider) {
    this.contextProvider = contextProvider;
    this.stabilityAiApiProvider = stabilityAiApiProvider;
    this.aiGenerationDaoProvider = aiGenerationDaoProvider;
  }

  @Override
  public AiGenerationRepositoryImpl get() {
    return newInstance(contextProvider.get(), stabilityAiApiProvider.get(), aiGenerationDaoProvider.get());
  }

  public static AiGenerationRepositoryImpl_Factory create(
      javax.inject.Provider<Context> contextProvider,
      javax.inject.Provider<StabilityAiApi> stabilityAiApiProvider,
      javax.inject.Provider<AiGenerationDao> aiGenerationDaoProvider) {
    return new AiGenerationRepositoryImpl_Factory(Providers.asDaggerProvider(contextProvider), Providers.asDaggerProvider(stabilityAiApiProvider), Providers.asDaggerProvider(aiGenerationDaoProvider));
  }

  public static AiGenerationRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<StabilityAiApi> stabilityAiApiProvider,
      Provider<AiGenerationDao> aiGenerationDaoProvider) {
    return new AiGenerationRepositoryImpl_Factory(contextProvider, stabilityAiApiProvider, aiGenerationDaoProvider);
  }

  public static AiGenerationRepositoryImpl newInstance(Context context,
      StabilityAiApi stabilityAiApi, AiGenerationDao aiGenerationDao) {
    return new AiGenerationRepositoryImpl(context, stabilityAiApi, aiGenerationDao);
  }
}
