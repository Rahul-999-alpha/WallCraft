package com.rahul.clearwalls.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.rahul.clearwalls.BuildConfig

@Composable
fun AdBanner(
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                val displayMetrics = context.resources.displayMetrics
                val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth))
                adUnitId = BuildConfig.ADMOB_BANNER_ID

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        tag = "loaded"
                        Log.d("AdBanner", "Banner ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        tag = "failed"
                        Log.e("AdBanner", "Banner ad FAILED: code=${error.code}, message=${error.message}, domain=${error.domain}, cause=${error.cause}")
                    }

                    override fun onAdOpened() {
                        Log.d("AdBanner", "Banner ad opened")
                    }

                    override fun onAdClicked() {
                        Log.d("AdBanner", "Banner ad clicked")
                    }
                }

                loadAd(AdRequest.Builder().build())
                Log.d("AdBanner", "Banner ad requested with unit ID: $adUnitId")
            }
        },
        update = { adView ->
            // Retry if previous load failed (e.g. SDK wasn't ready at first factory call)
            if (adView.tag == "failed") {
                Log.d("AdBanner", "Retrying banner ad load...")
                adView.tag = "retrying"
                adView.loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}
