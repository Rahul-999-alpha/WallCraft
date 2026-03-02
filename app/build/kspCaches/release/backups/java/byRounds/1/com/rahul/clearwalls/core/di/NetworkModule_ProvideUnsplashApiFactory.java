package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.remote.api.UnsplashApi;
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
public final class NetworkModule_ProvideUnsplashApiFactory implements Factory<UnsplashApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideUnsplashApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public UnsplashApi get() {
    return provideUnsplashApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvideUnsplashApiFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideUnsplashApiFactory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static NetworkModule_ProvideUnsplashApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideUnsplashApiFactory(okHttpClientProvider);
  }

  public static UnsplashApi provideUnsplashApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideUnsplashApi(okHttpClient));
  }
}
