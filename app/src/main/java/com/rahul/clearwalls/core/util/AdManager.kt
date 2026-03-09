package com.rahul.clearwalls.core.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.rahul.clearwalls.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central manager for all AdMob ad types.
 *
 * Fixes applied:
 *  BUG-004 — Cold-start App Open ad: after load completes, shows immediately if foregrounded.
 *  BUG-005 — currentActivity cleared in onActivityPaused (not just onActivityDestroyed).
 *  BUG-006 — currentActivity set ONLY in onActivityResumed, never onActivityStarted.
 *  BUG-007 — App Open ad cooldown: minimum 4 hours between shows per session.
 *  BUG-008 — Single source of truth for currentActivity; ClearWallsApp has no tracking.
 *  BUG-005 — Log strings use ASCII prefixes; no 4-byte emoji subject to R8 truncation.
 */
@Singleton
class AdManager @Inject constructor() {

    companion object {
        private const val TAG = "AdManager"
        private const val APP_OPEN_MIN_INTERVAL_MS = 4 * 60 * 60 * 1000L
        private const val APP_OPEN_COLD_START_DELAY_MS = 5_000L
    }

    // ── State ────────────────────────────────────────────────────────────────

    private var currentActivity: Activity? = null

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var appOpenAd: AppOpenAd? = null

    private var isAppOpenAdLoading = false
    private var lastAppOpenAdShownMs = 0L
    private val appStartMs = System.currentTimeMillis()

    data class AdConfig(
        val bannerEnabled: Boolean       = true,
        val interstitialEnabled: Boolean = true,
        val rewardedEnabled: Boolean     = true,
        val nativeEnabled: Boolean       = true,
        val isPremium: Boolean           = false,
    )

    @Volatile private var adConfig = AdConfig()

    fun updateConfig(config: AdConfig) { adConfig = config }
    fun updatePremiumState(isPremium: Boolean) { adConfig = adConfig.copy(isPremium = isPremium) }

    // ── Activity tracking ────────────────────────────────────────────────────

    fun registerActivityTracking(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(a: Activity, b: Bundle?) {}
            override fun onActivityStarted(a: Activity) {}
            override fun onActivityResumed(a: Activity) { currentActivity = a }
            override fun onActivityPaused(a: Activity) { if (currentActivity == a) currentActivity = null }
            override fun onActivityStopped(a: Activity) { if (currentActivity == a) currentActivity = null }
            override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
            override fun onActivityDestroyed(a: Activity) { if (currentActivity == a) currentActivity = null }
        })
    }

    // ── App Open ad ──────────────────────────────────────────────────────────

    fun loadAppOpenAd() {
        if (isAppOpenAdLoading || appOpenAd != null) return
        if (adConfig.isPremium) return
        isAppOpenAdLoading = true
        Log.d(TAG, "[APP_OPEN] Loading...")

        AppOpenAd.load(
            currentActivity ?: return Unit.also { isAppOpenAdLoading = false },
            BuildConfig.ADMOB_APP_OPEN_ID,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "[APP_OPEN] Loaded successfully")
                    appOpenAd = ad
                    isAppOpenAdLoading = false

                    // BUG-004 FIX: cold-start — show immediately if foregrounded
                    if (ProcessLifecycleOwner.get().lifecycle.currentState
                            .isAtLeast(Lifecycle.State.STARTED)) {
                        showAppOpenAd()
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "[APP_OPEN] FAILED: code=${error.code}, message=${error.message}")
                    appOpenAd = null
                    isAppOpenAdLoading = false
                }
            }
        )
    }

    fun onAppForegrounded() { showAppOpenAd() }

    private fun showAppOpenAd() {
        val ad = appOpenAd ?: return
        val activity = currentActivity ?: return
        if (adConfig.isPremium || !adConfig.interstitialEnabled) return

        val now = System.currentTimeMillis()
        if (now - appStartMs < APP_OPEN_COLD_START_DELAY_MS) return
        if (now - lastAppOpenAdShownMs < APP_OPEN_MIN_INTERVAL_MS) return

        Log.d(TAG, "[APP_OPEN] Showing")
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                loadAppOpenAd()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[APP_OPEN] Failed to show: code=${error.code}, message=${error.message}")
                appOpenAd = null
                loadAppOpenAd()
            }
            override fun onAdShowedFullScreenContent() {
                lastAppOpenAdShownMs = System.currentTimeMillis()
            }
        }
        ad.show(activity)
    }

    // ── Interstitial ad ──────────────────────────────────────────────────────

    fun preloadInterstitial() {
        if (adConfig.isPremium || !adConfig.interstitialEnabled) return
        Log.d(TAG, "[INTERSTITIAL] Preloading...")

        InterstitialAd.load(
            currentActivity ?: return,
            BuildConfig.ADMOB_INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "[INTERSTITIAL] Loaded successfully")
                    interstitialAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "[INTERSTITIAL] FAILED: code=${error.code}, message=${error.message}")
                    interstitialAd = null
                }
            }
        )
    }

    fun showInterstitial(onDismissed: () -> Unit = {}) {
        val ad = interstitialAd
        val activity = currentActivity
        Log.d(TAG, "[INTERSTITIAL] showInterstitial called, ad loaded: ${ad != null}")
        if (adConfig.isPremium || !adConfig.interstitialEnabled || ad == null || activity == null) {
            onDismissed()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                onDismissed()
                preloadInterstitial()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[INTERSTITIAL] Failed to show: code=${error.code}, message=${error.message}")
                interstitialAd = null
                onDismissed()
            }
        }
        ad.show(activity)
    }

    // ── Rewarded ad ──────────────────────────────────────────────────────────

    fun preloadRewarded() {
        if (adConfig.isPremium || !adConfig.rewardedEnabled) return
        Log.d(TAG, "[REWARDED] Preloading...")

        RewardedAd.load(
            currentActivity ?: return,
            BuildConfig.ADMOB_REWARDED_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "[REWARDED] Loaded successfully")
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "[REWARDED] FAILED: code=${error.code}, message=${error.message}")
                    rewardedAd = null
                }
            }
        )
    }

    fun showRewarded(onRewarded: () -> Unit, onDismissed: () -> Unit = {}) {
        val ad = rewardedAd
        val activity = currentActivity
        Log.d(TAG, "[REWARDED] showRewarded called, ad loaded: ${ad != null}")
        if (adConfig.isPremium || !adConfig.rewardedEnabled || ad == null || activity == null) {
            onDismissed()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                onDismissed()
                preloadRewarded()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "[REWARDED] Failed to show: code=${error.code}, message=${error.message}")
                rewardedAd = null
                onDismissed()
            }
        }
        ad.show(activity) { rewardItem ->
            Log.d(TAG, "[REWARDED] User rewarded: ${rewardItem.amount} ${rewardItem.type}")
            onRewarded()
        }
    }

    // ── Utility ──────────────────────────────────────────────────────────────

    fun isInterstitialLoaded() = interstitialAd != null
    fun isRewardedLoaded()     = rewardedAd != null
    fun isAppOpenAdLoaded()    = appOpenAd != null
}
