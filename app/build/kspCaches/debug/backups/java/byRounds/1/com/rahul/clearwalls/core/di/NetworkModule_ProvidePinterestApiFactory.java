package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.remote.api.PinterestApi;
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
public final class NetworkModule_ProvidePinterestApiFactory implements Factory<PinterestApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvidePinterestApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public PinterestApi get() {
    return providePinterestApi(okHttpClientProvider.get());
  }

  public static NetworkModule_ProvidePinterestApiFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePinterestApiFactory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static NetworkModule_ProvidePinterestApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvidePinterestApiFactory(okHttpClientProvider);
  }

  public static PinterestApi providePinterestApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.providePinterestApi(okHttpClient));
  }
}
