package com.rahul.clearwalls.data.repository;

import com.rahul.clearwalls.data.remote.api.FreepikApi;
import com.rahul.clearwalls.data.remote.api.PexelsApi;
import com.rahul.clearwalls.data.remote.api.PixabayApi;
import com.rahul.clearwalls.data.remote.api.UnsplashApi;
import com.rahul.clearwalls.data.remote.api.WallhavenApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class WallpaperRepositoryImpl_Factory implements Factory<WallpaperRepositoryImpl> {
  private final Provider<PixabayApi> pixabayApiProvider;

  private final Provider<WallhavenApi> wallhavenApiProvider;

  private final Provider<PexelsApi> pexelsApiProvider;

  private final Provider<UnsplashApi> unsplashApiProvider;

  private final Provider<FreepikApi> freepikApiProvider;

  public WallpaperRepositoryImpl_Factory(Provider<PixabayApi> pixabayApiProvider,
      Provider<WallhavenApi> wallhavenApiProvider, Provider<PexelsApi> pexelsApiProvider,
      Provider<UnsplashApi> unsplashApiProvider, Provider<FreepikApi> freepikApiProvider) {
    this.pixabayApiProvider = pixabayApiProvider;
    this.wallhavenApiProvider = wallhavenApiProvider;
    this.pexelsApiProvider = pexelsApiProvider;
    this.unsplashApiProvider = unsplashApiProvider;
    this.freepikApiProvider = freepikApiProvider;
  }

  @Override
  public WallpaperRepositoryImpl get() {
    return newInstance(pixabayApiProvider.get(), wallhavenApiProvider.get(), pexelsApiProvider.get(), unsplashApiProvider.get(), freepikApiProvider.get());
  }

  public static WallpaperRepositoryImpl_Factory create(
      javax.inject.Provider<PixabayApi> pixabayApiProvider,
      javax.inject.Provider<WallhavenApi> wallhavenApiProvider,
      javax.inject.Provider<PexelsApi> pexelsApiProvider,
      javax.inject.Provider<UnsplashApi> unsplashApiProvider,
      javax.inject.Provider<FreepikApi> freepikApiProvider) {
    return new WallpaperRepositoryImpl_Factory(Providers.asDaggerProvider(pixabayApiProvider), Providers.asDaggerProvider(wallhavenApiProvider), Providers.asDaggerProvider(pexelsApiProvider), Providers.asDaggerProvider(unsplashApiProvider), Providers.asDaggerProvider(freepikApiProvider));
  }

  public static WallpaperRepositoryImpl_Factory create(Provider<PixabayApi> pixabayApiProvider,
      Provider<WallhavenApi> wallhavenApiProvider, Provider<PexelsApi> pexelsApiProvider,
      Provider<UnsplashApi> unsplashApiProvider, Provider<FreepikApi> freepikApiProvider) {
    return new WallpaperRepositoryImpl_Factory(pixabayApiProvider, wallhavenApiProvider, pexelsApiProvider, unsplashApiProvider, freepikApiProvider);
  }

  public static WallpaperRepositoryImpl newInstance(PixabayApi pixabayApi,
      WallhavenApi wallhavenApi, PexelsApi pexelsApi, UnsplashApi unsplashApi,
      FreepikApi freepikApi) {
    return new WallpaperRepositoryImpl(pixabayApi, wallhavenApi, pexelsApi, unsplashApi, freepikApi);
  }
}
