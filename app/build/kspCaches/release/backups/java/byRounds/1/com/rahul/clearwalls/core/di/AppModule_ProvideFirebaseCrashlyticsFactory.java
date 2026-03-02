package com.rahul.clearwalls.core.di;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideFirebaseCrashlyticsFactory implements Factory<FirebaseCrashlytics> {
  @Override
  public FirebaseCrashlytics get() {
    return provideFirebaseCrashlytics();
  }

  public static AppModule_ProvideFirebaseCrashlyticsFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FirebaseCrashlytics provideFirebaseCrashlytics() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFirebaseCrashlytics());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideFirebaseCrashlyticsFactory INSTANCE = new AppModule_ProvideFirebaseCrashlyticsFactory();
  }
}
