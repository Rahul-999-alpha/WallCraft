package com.rahul.clearwalls.domain.usecase;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DownloadWallpaperUseCase_Factory implements Factory<DownloadWallpaperUseCase> {
  private final Provider<Context> contextProvider;

  public DownloadWallpaperUseCase_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DownloadWallpaperUseCase get() {
    return newInstance(contextProvider.get());
  }

  public static DownloadWallpaperUseCase_Factory create(
      javax.inject.Provider<Context> contextProvider) {
    return new DownloadWallpaperUseCase_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static DownloadWallpaperUseCase_Factory create(Provider<Context> contextProvider) {
    return new DownloadWallpaperUseCase_Factory(contextProvider);
  }

  public static DownloadWallpaperUseCase newInstance(Context context) {
    return new DownloadWallpaperUseCase(context);
  }
}
