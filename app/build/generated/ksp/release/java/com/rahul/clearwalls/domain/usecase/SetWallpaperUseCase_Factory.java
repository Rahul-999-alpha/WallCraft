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
public final class SetWallpaperUseCase_Factory implements Factory<SetWallpaperUseCase> {
  private final Provider<Context> contextProvider;

  public SetWallpaperUseCase_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SetWallpaperUseCase get() {
    return newInstance(contextProvider.get());
  }

  public static SetWallpaperUseCase_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new SetWallpaperUseCase_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static SetWallpaperUseCase_Factory create(Provider<Context> contextProvider) {
    return new SetWallpaperUseCase_Factory(contextProvider);
  }

  public static SetWallpaperUseCase newInstance(Context context) {
    return new SetWallpaperUseCase(context);
  }
}
