package com.rahul.clearwalls.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao;
import com.rahul.clearwalls.data.remote.api.PexelsApi;
import com.rahul.clearwalls.data.remote.api.PixabayApi;
import dagger.internal.DaggerGenerated;
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
public final class WallpaperRefreshWorker_Factory {
  private final Provider<PixabayApi> pixabayApiProvider;

  private final Provider<PexelsApi> pexelsApiProvider;

  private final Provider<CachedWallpaperDao> cachedWallpaperDaoProvider;

  public WallpaperRefreshWorker_Factory(Provider<PixabayApi> pixabayApiProvider,
      Provider<PexelsApi> pexelsApiProvider,
      Provider<CachedWallpaperDao> cachedWallpaperDaoProvider) {
    this.pixabayApiProvider = pixabayApiProvider;
    this.pexelsApiProvider = pexelsApiProvider;
    this.cachedWallpaperDaoProvider = cachedWallpaperDaoProvider;
  }

  public WallpaperRefreshWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, pixabayApiProvider.get(), pexelsApiProvider.get(), cachedWallpaperDaoProvider.get());
  }

  public static WallpaperRefreshWorker_Factory create(
      javax.inject.Provider<PixabayApi> pixabayApiProvider,
      javax.inject.Provider<PexelsApi> pexelsApiProvider,
      javax.inject.Provider<CachedWallpaperDao> cachedWallpaperDaoProvider) {
    return new WallpaperRefreshWorker_Factory(Providers.asDaggerProvider(pixabayApiProvider), Providers.asDaggerProvider(pexelsApiProvider), Providers.asDaggerProvider(cachedWallpaperDaoProvider));
  }

  public static WallpaperRefreshWorker_Factory create(Provider<PixabayApi> pixabayApiProvider,
      Provider<PexelsApi> pexelsApiProvider,
      Provider<CachedWallpaperDao> cachedWallpaperDaoProvider) {
    return new WallpaperRefreshWorker_Factory(pixabayApiProvider, pexelsApiProvider, cachedWallpaperDaoProvider);
  }

  public static WallpaperRefreshWorker newInstance(Context context, WorkerParameters params,
      PixabayApi pixabayApi, PexelsApi pexelsApi, CachedWallpaperDao cachedWallpaperDao) {
    return new WallpaperRefreshWorker(context, params, pixabayApi, pexelsApi, cachedWallpaperDao);
  }
}
