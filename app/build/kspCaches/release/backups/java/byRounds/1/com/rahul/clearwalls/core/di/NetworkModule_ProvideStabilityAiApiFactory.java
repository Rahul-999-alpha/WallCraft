package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.remote.api.StabilityAiApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideStabilityAiApiFactory implements Factory<StabilityAiApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideStabilityAiApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public StabilityAiApi get() {
    return provideStabilityAiApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvideStabilityAiApiFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideStabilityAiApiFactory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static NetworkModule_ProvideStabilityAiApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideStabilityAiApiFactory(okHttpClientProvider);
  }

  public static StabilityAiApi provideStabilityAiApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideStabilityAiApi(okHttpClient));
  }
}
