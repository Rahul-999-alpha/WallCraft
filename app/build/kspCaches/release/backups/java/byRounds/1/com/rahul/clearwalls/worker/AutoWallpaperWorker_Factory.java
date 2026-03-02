package com.rahul.clearwalls.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.rahul.clearwalls.data.local.dao.FavoriteDao;
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
public final class AutoWallpaperWorker_Factory {
  private final Provider<FavoriteDao> favoriteDaoProvider;

  public AutoWallpaperWorker_Factory(Provider<FavoriteDao> favoriteDaoProvider) {
    this.favoriteDaoProvider = favoriteDaoProvider;
  }

  public AutoWallpaperWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, favoriteDaoProvider.get());
  }

  public static AutoWallpaperWorker_Factory create(
      javax.inject.Provider<FavoriteDao> favoriteDaoProvider) {
    return new AutoWallpaperWorker_Factory(Providers.asDaggerProvider(favoriteDaoProvider));
  }

  public static AutoWallpaperWorker_Factory create(Provider<FavoriteDao> favoriteDaoProvider) {
    return new AutoWallpaperWorker_Factory(favoriteDaoProvider);
  }

  public static AutoWallpaperWorker newInstance(Context context, WorkerParameters params,
      FavoriteDao favoriteDao) {
    return new AutoWallpaperWorker(context, params, favoriteDao);
  }
}
