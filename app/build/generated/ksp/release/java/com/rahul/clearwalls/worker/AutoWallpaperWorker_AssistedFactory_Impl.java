package com.rahul.clearwalls.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AutoWallpaperWorker_AssistedFactory_Impl implements AutoWallpaperWorker_AssistedFactory {
  private final AutoWallpaperWorker_Factory delegateFactory;

  AutoWallpaperWorker_AssistedFactory_Impl(AutoWallpaperWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public AutoWallpaperWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<AutoWallpaperWorker_AssistedFactory> create(
      AutoWallpaperWorker_Factory delegateFactory) {
    return InstanceFactory.create(new AutoWallpaperWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<AutoWallpaperWorker_AssistedFactory> createFactoryProvider(
      AutoWallpaperWorker_Factory delegateFactory) {
    return InstanceFactory.create(new AutoWallpaperWorker_AssistedFactory_Impl(delegateFactory));
  }
}
