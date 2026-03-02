package com.rahul.clearwalls.core.util;

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
public final class AdminConfigManager_Factory implements Factory<AdminConfigManager> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public AdminConfigManager_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public AdminConfigManager get() {
    return newInstance(dataStoreProvider.get());
  }

  public static AdminConfigManager_Factory create(
      javax.inject.Provider<DataStore<Preferences>> dataStoreProvider) {
    return new AdminConfigManager_Factory(Providers.asDaggerProvider(dataStoreProvider));
  }

  public static AdminConfigManager_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new AdminConfigManager_Factory(dataStoreProvider);
  }

  public static AdminConfigManager newInstance(DataStore<Preferences> dataStore) {
    return new AdminConfigManager(dataStore);
  }
}
