package com.rahul.clearwalls.core.di;

import com.rahul.clearwalls.data.local.ClearWallsDatabase;
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCachedWallpaperDaoFactory implements Factory<CachedWallpaperDao> {
  private final Provider<ClearWallsDatabase> databaseProvider;

  public DatabaseModule_ProvideCachedWallpaperDaoFactory(
      Provider<ClearWallsDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CachedWallpaperDao get() {
    return provideCachedWallpaperDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCachedWallpaperDaoFactory create(
      javax.inject.Provider<ClearWallsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCachedWallpaperDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideCachedWallpaperDaoFactory create(
      Provider<ClearWallsDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCachedWallpaperDaoFactory(databaseProvider);
  }

  public static CachedWallpaperDao provideCachedWallpaperDao(ClearWallsDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCachedWallpaperDao(database));
  }
}
