package com.rahul.clearwalls.core.di;

import android.content.Context;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideFirebaseAnalyticsFactory implements Factory<FirebaseAnalytics> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideFirebaseAnalyticsFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FirebaseAnalytics get() {
    return provideFirebaseAnalytics(contextProvider.get());
  }

  public static AppModule_ProvideFirebaseAnalyticsFactory create(
      javax.inject.Provider<Context> contextProvider) {
    return new AppModule_ProvideFirebaseAnalyticsFactory(Providers.asDaggerProvider(contextProvider));
  }

  public static AppModule_ProvideFirebaseAnalyticsFactory create(
      Provider<Context> contextProvider) {
    return new AppModule_ProvideFirebaseAnalyticsFactory(contextProvider);
  }

  public static FirebaseAnalytics provideFirebaseAnalytics(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFirebaseAnalytics(context));
  }
}
