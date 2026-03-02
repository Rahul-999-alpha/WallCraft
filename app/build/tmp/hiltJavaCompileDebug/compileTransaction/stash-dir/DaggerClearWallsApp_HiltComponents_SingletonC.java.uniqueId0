package com.rahul.clearwalls;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.rahul.clearwalls.core.di.AppModule_ProvideDataStoreFactory;
import com.rahul.clearwalls.core.di.DatabaseModule_ProvideAiGenerationDaoFactory;
import com.rahul.clearwalls.core.di.DatabaseModule_ProvideCachedWallpaperDaoFactory;
import com.rahul.clearwalls.core.di.DatabaseModule_ProvideDatabaseFactory;
import com.rahul.clearwalls.core.di.DatabaseModule_ProvideFavoriteDaoFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvideFreepikApiFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvideOkHttpClientFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvidePexelsApiFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvidePixabayApiFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvideStabilityAiApiFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvideUnsplashApiFactory;
import com.rahul.clearwalls.core.di.NetworkModule_ProvideWallhavenApiFactory;
import com.rahul.clearwalls.core.util.AdManager;
import com.rahul.clearwalls.core.util.AdminConfigManager;
import com.rahul.clearwalls.data.local.ClearWallsDatabase;
import com.rahul.clearwalls.data.local.dao.AiGenerationDao;
import com.rahul.clearwalls.data.local.dao.CachedWallpaperDao;
import com.rahul.clearwalls.data.local.dao.FavoriteDao;
import com.rahul.clearwalls.data.remote.api.FreepikApi;
import com.rahul.clearwalls.data.remote.api.PexelsApi;
import com.rahul.clearwalls.data.remote.api.PixabayApi;
import com.rahul.clearwalls.data.remote.api.StabilityAiApi;
import com.rahul.clearwalls.data.remote.api.UnsplashApi;
import com.rahul.clearwalls.data.remote.api.WallhavenApi;
import com.rahul.clearwalls.data.repository.AiGenerationRepositoryImpl;
import com.rahul.clearwalls.data.repository.FavoriteRepositoryImpl;
import com.rahul.clearwalls.data.repository.WallpaperRepositoryImpl;
import com.rahul.clearwalls.domain.usecase.DownloadWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.GenerateAiWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.GetAiQuotaUseCase;
import com.rahul.clearwalls.domain.usecase.GetCategoriesUseCase;
import com.rahul.clearwalls.domain.usecase.GetEditorPicksUseCase;
import com.rahul.clearwalls.domain.usecase.GetWallpapersUseCase;
import com.rahul.clearwalls.domain.usecase.SearchWallpapersUseCase;
import com.rahul.clearwalls.domain.usecase.SetWallpaperUseCase;
import com.rahul.clearwalls.domain.usecase.ToggleFavoriteUseCase;
import com.rahul.clearwalls.presentation.MainActivity;
import com.rahul.clearwalls.presentation.MainActivity_MembersInjector;
import com.rahul.clearwalls.presentation.admin.AdminViewModel;
import com.rahul.clearwalls.presentation.admin.AdminViewModel_HiltModules;
import com.rahul.clearwalls.presentation.admin.AdminViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.admin.AdminViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.aigenerate.AiGenerateViewModel;
import com.rahul.clearwalls.presentation.aigenerate.AiGenerateViewModel_HiltModules;
import com.rahul.clearwalls.presentation.aigenerate.AiGenerateViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.aigenerate.AiGenerateViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.browse.BrowseViewModel;
import com.rahul.clearwalls.presentation.browse.BrowseViewModel_HiltModules;
import com.rahul.clearwalls.presentation.browse.BrowseViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.browse.BrowseViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.detail.WallpaperDetailViewModel;
import com.rahul.clearwalls.presentation.detail.WallpaperDetailViewModel_HiltModules;
import com.rahul.clearwalls.presentation.detail.WallpaperDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.detail.WallpaperDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.favorites.FavoritesViewModel;
import com.rahul.clearwalls.presentation.favorites.FavoritesViewModel_HiltModules;
import com.rahul.clearwalls.presentation.favorites.FavoritesViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.favorites.FavoritesViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.home.HomeViewModel;
import com.rahul.clearwalls.presentation.home.HomeViewModel_HiltModules;
import com.rahul.clearwalls.presentation.home.HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.home.HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.search.SearchViewModel;
import com.rahul.clearwalls.presentation.search.SearchViewModel_HiltModules;
import com.rahul.clearwalls.presentation.search.SearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.search.SearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.presentation.settings.SettingsViewModel;
import com.rahul.clearwalls.presentation.settings.SettingsViewModel_HiltModules;
import com.rahul.clearwalls.presentation.settings.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.rahul.clearwalls.presentation.settings.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.rahul.clearwalls.worker.AutoWallpaperWorker;
import com.rahul.clearwalls.worker.AutoWallpaperWorker_AssistedFactory;
import com.rahul.clearwalls.worker.WallpaperRefreshWorker;
import com.rahul.clearwalls.worker.WallpaperRefreshWorker_AssistedFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerClearWallsApp_HiltComponents_SingletonC {
  private DaggerClearWallsApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ClearWallsApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ClearWallsApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ClearWallsApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ClearWallsApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ClearWallsApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ClearWallsApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ClearWallsApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ClearWallsApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ClearWallsApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ClearWallsApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ClearWallsApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ClearWallsApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ClearWallsApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(8).put(AdminViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AdminViewModel_HiltModules.KeyModule.provide()).put(AiGenerateViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AiGenerateViewModel_HiltModules.KeyModule.provide()).put(BrowseViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BrowseViewModel_HiltModules.KeyModule.provide()).put(FavoritesViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FavoritesViewModel_HiltModules.KeyModule.provide()).put(HomeViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HomeViewModel_HiltModules.KeyModule.provide()).put(SearchViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SearchViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).put(WallpaperDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, WallpaperDetailViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectDataStore(instance, singletonCImpl.provideDataStoreProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends ClearWallsApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AdminViewModel> adminViewModelProvider;

    private Provider<AiGenerateViewModel> aiGenerateViewModelProvider;

    private Provider<BrowseViewModel> browseViewModelProvider;

    private Provider<FavoritesViewModel> favoritesViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<WallpaperDetailViewModel> wallpaperDetailViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GenerateAiWallpaperUseCase generateAiWallpaperUseCase() {
      return new GenerateAiWallpaperUseCase(singletonCImpl.aiGenerationRepositoryImplProvider.get());
    }

    private GetAiQuotaUseCase getAiQuotaUseCase() {
      return new GetAiQuotaUseCase(singletonCImpl.aiGenerationRepositoryImplProvider.get());
    }

    private GetWallpapersUseCase getWallpapersUseCase() {
      return new GetWallpapersUseCase(singletonCImpl.wallpaperRepositoryImplProvider.get());
    }

    private GetCategoriesUseCase getCategoriesUseCase() {
      return new GetCategoriesUseCase(singletonCImpl.wallpaperRepositoryImplProvider.get());
    }

    private ToggleFavoriteUseCase toggleFavoriteUseCase() {
      return new ToggleFavoriteUseCase(singletonCImpl.favoriteRepositoryImplProvider.get());
    }

    private GetEditorPicksUseCase getEditorPicksUseCase() {
      return new GetEditorPicksUseCase(singletonCImpl.wallpaperRepositoryImplProvider.get());
    }

    private SearchWallpapersUseCase searchWallpapersUseCase() {
      return new SearchWallpapersUseCase(singletonCImpl.wallpaperRepositoryImplProvider.get());
    }

    private SetWallpaperUseCase setWallpaperUseCase() {
      return new SetWallpaperUseCase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    private DownloadWallpaperUseCase downloadWallpaperUseCase() {
      return new DownloadWallpaperUseCase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.adminViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.aiGenerateViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.browseViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.favoritesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.wallpaperDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(8).put(AdminViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) adminViewModelProvider)).put(AiGenerateViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) aiGenerateViewModelProvider)).put(BrowseViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) browseViewModelProvider)).put(FavoritesViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) favoritesViewModelProvider)).put(HomeViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) homeViewModelProvider)).put(SearchViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) searchViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).put(WallpaperDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) wallpaperDetailViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rahul.clearwalls.presentation.admin.AdminViewModel 
          return (T) new AdminViewModel(singletonCImpl.adminConfigManagerProvider.get());

          case 1: // com.rahul.clearwalls.presentation.aigenerate.AiGenerateViewModel 
          return (T) new AiGenerateViewModel(viewModelCImpl.generateAiWallpaperUseCase(), viewModelCImpl.getAiQuotaUseCase(), singletonCImpl.aiGenerationRepositoryImplProvider.get());

          case 2: // com.rahul.clearwalls.presentation.browse.BrowseViewModel 
          return (T) new BrowseViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getWallpapersUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.toggleFavoriteUseCase());

          case 3: // com.rahul.clearwalls.presentation.favorites.FavoritesViewModel 
          return (T) new FavoritesViewModel(singletonCImpl.favoriteRepositoryImplProvider.get(), viewModelCImpl.toggleFavoriteUseCase());

          case 4: // com.rahul.clearwalls.presentation.home.HomeViewModel 
          return (T) new HomeViewModel(viewModelCImpl.getWallpapersUseCase(), viewModelCImpl.getEditorPicksUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.toggleFavoriteUseCase(), singletonCImpl.favoriteRepositoryImplProvider.get());

          case 5: // com.rahul.clearwalls.presentation.search.SearchViewModel 
          return (T) new SearchViewModel(viewModelCImpl.searchWallpapersUseCase(), viewModelCImpl.toggleFavoriteUseCase());

          case 6: // com.rahul.clearwalls.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideDataStoreProvider.get());

          case 7: // com.rahul.clearwalls.presentation.detail.WallpaperDetailViewModel 
          return (T) new WallpaperDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.wallpaperRepositoryImplProvider.get(), singletonCImpl.favoriteRepositoryImplProvider.get(), viewModelCImpl.setWallpaperUseCase(), viewModelCImpl.downloadWallpaperUseCase(), viewModelCImpl.toggleFavoriteUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ClearWallsApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ClearWallsApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ClearWallsApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AdManager> adManagerProvider;

    private Provider<ClearWallsDatabase> provideDatabaseProvider;

    private Provider<AutoWallpaperWorker_AssistedFactory> autoWallpaperWorker_AssistedFactoryProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<PixabayApi> providePixabayApiProvider;

    private Provider<PexelsApi> providePexelsApiProvider;

    private Provider<WallpaperRefreshWorker_AssistedFactory> wallpaperRefreshWorker_AssistedFactoryProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<AdminConfigManager> adminConfigManagerProvider;

    private Provider<StabilityAiApi> provideStabilityAiApiProvider;

    private Provider<AiGenerationRepositoryImpl> aiGenerationRepositoryImplProvider;

    private Provider<WallhavenApi> provideWallhavenApiProvider;

    private Provider<UnsplashApi> provideUnsplashApiProvider;

    private Provider<FreepikApi> provideFreepikApiProvider;

    private Provider<WallpaperRepositoryImpl> wallpaperRepositoryImplProvider;

    private Provider<FavoriteRepositoryImpl> favoriteRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private FavoriteDao favoriteDao() {
      return DatabaseModule_ProvideFavoriteDaoFactory.provideFavoriteDao(provideDatabaseProvider.get());
    }

    private CachedWallpaperDao cachedWallpaperDao() {
      return DatabaseModule_ProvideCachedWallpaperDaoFactory.provideCachedWallpaperDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return ImmutableMap.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>of("com.rahul.clearwalls.worker.AutoWallpaperWorker", ((Provider) autoWallpaperWorker_AssistedFactoryProvider), "com.rahul.clearwalls.worker.WallpaperRefreshWorker", ((Provider) wallpaperRefreshWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private AiGenerationDao aiGenerationDao() {
      return DatabaseModule_ProvideAiGenerationDaoFactory.provideAiGenerationDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.adManagerProvider = DoubleCheck.provider(new SwitchingProvider<AdManager>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<ClearWallsDatabase>(singletonCImpl, 2));
      this.autoWallpaperWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<AutoWallpaperWorker_AssistedFactory>(singletonCImpl, 1));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 5));
      this.providePixabayApiProvider = DoubleCheck.provider(new SwitchingProvider<PixabayApi>(singletonCImpl, 4));
      this.providePexelsApiProvider = DoubleCheck.provider(new SwitchingProvider<PexelsApi>(singletonCImpl, 6));
      this.wallpaperRefreshWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<WallpaperRefreshWorker_AssistedFactory>(singletonCImpl, 3));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 7));
      this.adminConfigManagerProvider = DoubleCheck.provider(new SwitchingProvider<AdminConfigManager>(singletonCImpl, 8));
      this.provideStabilityAiApiProvider = DoubleCheck.provider(new SwitchingProvider<StabilityAiApi>(singletonCImpl, 10));
      this.aiGenerationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AiGenerationRepositoryImpl>(singletonCImpl, 9));
      this.provideWallhavenApiProvider = DoubleCheck.provider(new SwitchingProvider<WallhavenApi>(singletonCImpl, 12));
      this.provideUnsplashApiProvider = DoubleCheck.provider(new SwitchingProvider<UnsplashApi>(singletonCImpl, 13));
      this.provideFreepikApiProvider = DoubleCheck.provider(new SwitchingProvider<FreepikApi>(singletonCImpl, 14));
      this.wallpaperRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<WallpaperRepositoryImpl>(singletonCImpl, 11));
      this.favoriteRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<FavoriteRepositoryImpl>(singletonCImpl, 15));
    }

    @Override
    public void injectClearWallsApp(ClearWallsApp clearWallsApp) {
      injectClearWallsApp2(clearWallsApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private ClearWallsApp injectClearWallsApp2(ClearWallsApp instance) {
      ClearWallsApp_MembersInjector.injectAdManager(instance, adManagerProvider.get());
      ClearWallsApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rahul.clearwalls.core.util.AdManager 
          return (T) new AdManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.rahul.clearwalls.worker.AutoWallpaperWorker_AssistedFactory 
          return (T) new AutoWallpaperWorker_AssistedFactory() {
            @Override
            public AutoWallpaperWorker create(Context context, WorkerParameters params) {
              return new AutoWallpaperWorker(context, params, singletonCImpl.favoriteDao());
            }
          };

          case 2: // com.rahul.clearwalls.data.local.ClearWallsDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.rahul.clearwalls.worker.WallpaperRefreshWorker_AssistedFactory 
          return (T) new WallpaperRefreshWorker_AssistedFactory() {
            @Override
            public WallpaperRefreshWorker create(Context context2, WorkerParameters params2) {
              return new WallpaperRefreshWorker(context2, params2, singletonCImpl.providePixabayApiProvider.get(), singletonCImpl.providePexelsApiProvider.get(), singletonCImpl.cachedWallpaperDao());
            }
          };

          case 4: // com.rahul.clearwalls.data.remote.api.PixabayApi 
          return (T) NetworkModule_ProvidePixabayApiFactory.providePixabayApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 5: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.rahul.clearwalls.data.remote.api.PexelsApi 
          return (T) NetworkModule_ProvidePexelsApiFactory.providePexelsApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 7: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) AppModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.rahul.clearwalls.core.util.AdminConfigManager 
          return (T) new AdminConfigManager(singletonCImpl.provideDataStoreProvider.get());

          case 9: // com.rahul.clearwalls.data.repository.AiGenerationRepositoryImpl 
          return (T) new AiGenerationRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideStabilityAiApiProvider.get(), singletonCImpl.aiGenerationDao());

          case 10: // com.rahul.clearwalls.data.remote.api.StabilityAiApi 
          return (T) NetworkModule_ProvideStabilityAiApiFactory.provideStabilityAiApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 11: // com.rahul.clearwalls.data.repository.WallpaperRepositoryImpl 
          return (T) new WallpaperRepositoryImpl(singletonCImpl.providePixabayApiProvider.get(), singletonCImpl.provideWallhavenApiProvider.get(), singletonCImpl.providePexelsApiProvider.get(), singletonCImpl.provideUnsplashApiProvider.get(), singletonCImpl.provideFreepikApiProvider.get());

          case 12: // com.rahul.clearwalls.data.remote.api.WallhavenApi 
          return (T) NetworkModule_ProvideWallhavenApiFactory.provideWallhavenApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 13: // com.rahul.clearwalls.data.remote.api.UnsplashApi 
          return (T) NetworkModule_ProvideUnsplashApiFactory.provideUnsplashApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 14: // com.rahul.clearwalls.data.remote.api.FreepikApi 
          return (T) NetworkModule_ProvideFreepikApiFactory.provideFreepikApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 15: // com.rahul.clearwalls.data.repository.FavoriteRepositoryImpl 
          return (T) new FavoriteRepositoryImpl(singletonCImpl.favoriteDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
