package com.rahul.clearwalls.core.util;

import android.content.Context;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ImageQualityManager_Factory implements Factory<ImageQualityManager> {
  private final Provider<Context> contextProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public ImageQualityManager_Factory(Provider<Context> contextProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.contextProvider = contextProvider;
    this.networkMonitorProvider = networkMonitorProvider;
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public ImageQualityManager get() {
    return newInstance(contextProvider.get(), networkMonitorProvider.get(), dataStoreProvider.get());
  }

  public static ImageQualityManager_Factory create(javax.inject.Provider<Context> contextProvider,
      javax.inject.Provider<NetworkMonitor> networkMonitorProvider,
      javax.inject.Provider<DataStore<Preferences>> dataStoreProvider) {
    return new ImageQualityManager_Factory(Providers.asDaggerProvider(contextProvider), Providers.asDaggerProvider(networkMonitorProvider), Providers.asDaggerProvider(dataStoreProvider));
  }

  public static ImageQualityManager_Factory create(Provider<Context> contextProvider,
      Provider<NetworkMonitor> networkMonitorProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new ImageQualityManager_Factory(contextProvider, networkMonitorProvider, dataStoreProvider);
  }

  public static ImageQualityManager newInstance(Context context, NetworkMonitor networkMonitor,
      DataStore<Preferences> dataStore) {
    return new ImageQualityManager(context, networkMonitor, dataStore);
  }
}
