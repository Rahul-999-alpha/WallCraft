package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.remote.api.PexelsApi;
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
public final class NetworkModule_ProvidePexelsApiFactory implements Factory<PexelsApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvidePexelsApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public PexelsApi get() {
    return providePexelsApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvidePexelsApiFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePexelsApiFactory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static NetworkModule_ProvidePexelsApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePexelsApiFactory(okHttpClientProvider);
  }

  public static PexelsApi providePexelsApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.providePexelsApi(okHttpClient));
  }
}
