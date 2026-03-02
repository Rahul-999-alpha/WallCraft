package com.rahul.clearwalls.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                setAdSize(AdSize.BANNER)
                adUnitId = BuildConfig.ADMOB_BANNER_ID

                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d("AdBanner", "Banner ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdBanner", "Banner ad failed to load: ${error.message} (code: ${error.code})")
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
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}
