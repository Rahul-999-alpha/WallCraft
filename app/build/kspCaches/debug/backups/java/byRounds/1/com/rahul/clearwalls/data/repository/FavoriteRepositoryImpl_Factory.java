package com.rahul.clearwalls.data.repository;

import com.rahul.clearwalls.data.local.dao.FavoriteDao;
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
public final class FavoriteRepositoryImpl_Factory implements Factory<FavoriteRepositoryImpl> {
  private final Provider<FavoriteDao> favoriteDaoProvider;

  public FavoriteRepositoryImpl_Factory(Provider<FavoriteDao> favoriteDaoProvider) {
    this.favoriteDaoProvider = favoriteDaoProvider;
  }

  @Override
  public FavoriteRepositoryImpl get() {
    return newInstance(favoriteDaoProvider.get());
  }

  public static FavoriteRepositoryImpl_Factory create(
      javax.inject.Provider<FavoriteDao> favoriteDaoProvider) {
    return new FavoriteRepositoryImpl_Factory(Providers.asDaggerProvider(favoriteDaoProvider));
  }

  public static FavoriteRepositoryImpl_Factory create(Provider<FavoriteDao> favoriteDaoProvider) {
    return new FavoriteRepositoryImpl_Factory(favoriteDaoProvider);
  }

  public static FavoriteRepositoryImpl newInstance(FavoriteDao favoriteDao) {
    return new FavoriteRepositoryImpl(favoriteDao);
  }
}
