package com.rahul.clearwalls.core.util

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.rahul.clearwalls.BuildConfig
import com.rahul.clearwalls.core.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var appOpenAd: AppOpenAd? = null
    private var lastInterstitialTime = 0L
    private var lastAppOpenTime = 0L
    private var isShowingAd = false
    private var setWallpaperCount = 0
    private var downloadCount = 0
    private val firstSessionStart = System.currentTimeMillis()

    private val _nativeAd = MutableStateFlow<NativeAd?>(null)
    val nativeAd: StateFlow<NativeAd?> = _nativeAd.asStateFlow()

    fun preloadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            BuildConfig.ADMOB_INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun preloadRewarded() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            BuildConfig.ADMOB_REWARDED_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    fun loadAppOpenAd() {
        if (appOpenAd != null || isShowingAd) return

        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            BuildConfig.ADMOB_APP_OPEN_ID,
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    appOpenAd = null
                }
            }
        )
    }

    fun loadNativeAd() {
        val adLoader = AdLoader.Builder(context, BuildConfig.ADMOB_BANNER_ID)
            .forNativeAd { ad ->
                _nativeAd.value?.destroy()
                _nativeAd.value = ad
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun shouldShowInterstitialOnSet(): Boolean {
        setWallpaperCount++
        return shouldShowInterstitial(setWallpaperCount, Constants.AD_INTERSTITIAL_SET_INTERVAL)
    }

    fun shouldShowInterstitialOnDownload(): Boolean {
        downloadCount++
        return shouldShowInterstitial(downloadCount, Constants.AD_INTERSTITIAL_DOWNLOAD_INTERVAL)
    }

    private fun shouldShowInterstitial(count: Int, interval: Int): Boolean {
        val now = System.currentTimeMillis()
        if (now - firstSessionStart < Constants.AD_FIRST_SESSION_GRACE_MS) return false
        if (now - lastInterstitialTime < Constants.AD_INTERSTITIAL_COOLDOWN_MS) return false
        return count % interval == 0
    }

    fun showInterstitial(activity: Activity, onDismissed: () -> Unit = {}) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    lastInterstitialTime = System.currentTimeMillis()
                    interstitialAd = null
                    preloadInterstitial()
                    onDismissed()
                }
            }
            ad.show(activity)
        } else {
            onDismissed()
            preloadInterstitial()
        }
    }

    fun showRewarded(activity: Activity, onRewarded: () -> Unit, onFailed: () -> Unit = {}) {
        val ad = rewardedAd
        if (ad != null) {
            ad.show(activity) { _ ->
                rewardedAd = null
                preloadRewarded()
                onRewarded()
            }
        } else {
            onFailed()
            preloadRewarded()
        }
    }

    fun isRewardedReady(): Boolean = rewardedAd != null

    fun showAppOpenAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
        // Don't show if already showing an ad or recently shown
        if (isShowingAd) {
            onAdDismissed()
            return
        }

        val now = System.currentTimeMillis()

        // Grace period after first session start (don't show immediately on app launch)
        if (now - firstSessionStart < Constants.AD_FIRST_SESSION_GRACE_MS) {
            onAdDismissed()
            return
        }

        // Cooldown between app open ads (4 hours)
        if (now - lastAppOpenTime < 4 * 60 * 60 * 1000L) {
            onAdDismissed()
            return
        }

        val ad = appOpenAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    isShowingAd = false
                    lastAppOpenTime = System.currentTimeMillis()
                    appOpenAd = null
                    loadAppOpenAd()
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                    isShowingAd = false
                    appOpenAd = null
                    loadAppOpenAd()
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            ad.show(activity)
        } else {
            onAdDismissed()
            loadAppOpenAd()
        }
    }
}
