package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.remote.api.PixabayApi;
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
public final class NetworkModule_ProvidePixabayApiFactory implements Factory<PixabayApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvidePixabayApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public PixabayApi get() {
    return providePixabayApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvidePixabayApiFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePixabayApiFactory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static NetworkModule_ProvidePixabayApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePixabayApiFactory(okHttpClientProvider);
  }

  public static PixabayApi providePixabayApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.providePixabayApi(okHttpClient));
  }
}
