package com.rahul.clearwalls.presentation.admin;

import com.rahul.clearwalls.core.util.AdminConfigManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AdminViewModel_Factory implements Factory<AdminViewModel> {
  private final Provider<AdminConfigManager> adminConfigManagerProvider;

  public AdminViewModel_Factory(Provider<AdminConfigManager> adminConfigManagerProvider) {
    this.adminConfigManagerProvider = adminConfigManagerProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(adminConfigManagerProvider.get());
  }

  public static AdminViewModel_Factory create(
      javax.inject.Provider<AdminConfigManager> adminConfigManagerProvider) {
    return new AdminViewModel_Factory(Providers.asDaggerProvider(adminConfigManagerProvider));
  }

  public static AdminViewModel_Factory create(
      Provider<AdminConfigManager> adminConfigManagerProvider) {
    return new AdminViewModel_Factory(adminConfigManagerProvider);
  }

  public static AdminViewModel newInstance(AdminConfigManager adminConfigManager) {
    return new AdminViewModel(adminConfigManager);
  }
}
