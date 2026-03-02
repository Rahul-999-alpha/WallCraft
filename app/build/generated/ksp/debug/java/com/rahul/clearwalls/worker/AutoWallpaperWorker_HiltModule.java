package com.rahul.clearwalls.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = AutoWallpaperWorker.class
)
public interface AutoWallpaperWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.rahul.clearwalls.worker.AutoWallpaperWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      AutoWallpaperWorker_AssistedFactory factory);
}
