package com.rahul.clearwalls.core.util;

import android.content.Context;
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
public final class AdManager_Factory implements Factory<AdManager> {
  private final Provider<Context> contextProvider;

  public AdManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AdManager get() {
    return newInstance(contextProvider.get());
  }

  public static AdManager_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new AdManager_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static AdManager_Factory create(Provider<Context> contextProvider) {
    return new AdManager_Factory(contextProvider);
  }

  public static AdManager newInstance(Context context) {
    return new AdManager(context);
  }
}
