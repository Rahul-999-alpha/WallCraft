package com.rahul.clearwalls

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.rahul.clearwalls.core.common.Constants
import com.rahul.clearwalls.core.util.AdManager
import com.rahul.clearwalls.worker.WallpaperRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ClearWallsApp : Application(), Configuration.Provider, Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "ClearWallsApp"
    }

    @Inject lateinit var adManager: AdManager
    @Inject lateinit var workerFactory: HiltWorkerFactory

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        // Initialize Mobile Ads SDK and wait for completion before loading ads
        Log.d(TAG, "Initializing Mobile Ads SDK...")
        MobileAds.initialize(this) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for ((adapter, status) in statusMap) {
                Log.d(TAG, "Ad adapter: $adapter -> ${status.initializationState} (${status.description})")
            }
            Log.d(TAG, "Mobile Ads SDK initialized - now preloading ads")
            // Only preload ads AFTER SDK initialization completes
            adManager.preloadInterstitial()
            adManager.preloadRewarded()
            adManager.loadAppOpenAd()
        }

        scheduleWallpaperRefresh()
        setupAppOpenAds()
    }

    private fun setupAppOpenAds() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                // Show app open ad when app comes to foreground
                currentActivity?.let { activity ->
                    adManager.showAppOpenAd(activity)
                }
            }
        })
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun scheduleWallpaperRefresh() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val refreshWork = PeriodicWorkRequestBuilder<WallpaperRefreshWorker>(
            Constants.DEFAULT_REFRESH_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshWork
        )
    }

    // ActivityLifecycleCallbacks implementation
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
}
