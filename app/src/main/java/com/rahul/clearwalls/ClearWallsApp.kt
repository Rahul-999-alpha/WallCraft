package com.rahul.clearwalls

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
import com.rahul.clearwalls.worker.NewWallpaperNotificationWorker
import com.rahul.clearwalls.worker.WallpaperRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ClearWallsApp : android.app.Application(), Configuration.Provider {

    companion object {
        private const val TAG = "ClearWallsApp"
    }

    @Inject lateinit var adManager: AdManager
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // BUG-008 FIX: Activity lifecycle tracking is now owned entirely by AdManager.
        adManager.registerActivityTracking(this)

        Log.d(TAG, "[INIT] Starting Mobile Ads SDK initialisation...")
        MobileAds.initialize(this) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for ((adapter, status) in statusMap) {
                Log.d(TAG, "[ADMOB] Adapter: $adapter -> ${status.initializationState} (${status.description})")
            }
            Log.d(TAG, "[ADMOB] SDK initialised — preloading ads")

            adManager.preloadInterstitial()
            adManager.preloadRewarded()
            adManager.loadAppOpenAd()
        }

        scheduleWallpaperRefresh()
        scheduleNotificationWorker()
        setupAppOpenAds()
    }

    private fun setupAppOpenAds() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                adManager.onAppForegrounded()
            }
        })
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun scheduleNotificationWorker() {
        val work = PeriodicWorkRequestBuilder<NewWallpaperNotificationWorker>(
            Constants.NOTIFICATION_INTERVAL_HOURS, TimeUnit.HOURS,
            30, TimeUnit.MINUTES
        )
            .setInitialDelay(Constants.NOTIFICATION_INTERVAL_HOURS, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

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
}
