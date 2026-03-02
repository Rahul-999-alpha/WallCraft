package com.rahul.clearwalls;

import androidx.hilt.work.HiltWorkerFactory;
import com.rahul.clearwalls.core.util.AdManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class ClearWallsApp_MembersInjector implements MembersInjector<ClearWallsApp> {
  private final Provider<AdManager> adManagerProvider;

  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public ClearWallsApp_MembersInjector(Provider<AdManager> adManagerProvider,
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.adManagerProvider = adManagerProvider;
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<ClearWallsApp> create(Provider<AdManager> adManagerProvider,
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new ClearWallsApp_MembersInjector(adManagerProvider, workerFactoryProvider);
  }

  public static MembersInjector<ClearWallsApp> create(
      javax.inject.Provider<AdManager> adManagerProvider,
      javax.inject.Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new ClearWallsApp_MembersInjector(Providers.asDaggerProvider(adManagerProvider), Providers.asDaggerProvider(workerFactoryProvider));
  }

  @Override
  public void injectMembers(ClearWallsApp instance) {
    injectAdManager(instance, adManagerProvider.get());
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.rahul.clearwalls.ClearWallsApp.adManager")
  public static void injectAdManager(ClearWallsApp instance, AdManager adManager) {
    instance.adManager = adManager;
  }

  @InjectedFieldSignature("com.rahul.clearwalls.ClearWallsApp.workerFactory")
  public static void injectWorkerFactory(ClearWallsApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
