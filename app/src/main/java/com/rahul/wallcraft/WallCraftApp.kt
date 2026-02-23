package com.rahul.wallcraft

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.rahul.wallcraft.core.util.AdManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WallCraftApp : Application() {

    @Inject lateinit var adManager: AdManager

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        adManager.preloadInterstitial()
        adManager.preloadRewarded()
    }
}
