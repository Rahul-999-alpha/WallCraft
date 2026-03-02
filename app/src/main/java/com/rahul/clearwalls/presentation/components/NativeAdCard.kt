package com.rahul.clearwalls.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import android.widget.ImageView
import android.widget.TextView
import android.view.LayoutInflater
import com.rahul.clearwalls.BuildConfig

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AndroidView(
            factory = { context ->
                val adView = NativeAdView(context)
                val adLoader = AdLoader.Builder(context, BuildConfig.ADMOB_NATIVE_ID)
                    .forNativeAd { nativeAd ->
                        val headlineView = TextView(context).apply {
                            text = nativeAd.headline
                            textSize = 14f
                        }
                        adView.headlineView = headlineView
                        adView.addView(headlineView)
                        adView.setNativeAd(nativeAd)
                    }
                    .build()
                adLoader.loadAd(AdRequest.Builder().build())
                adView
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}
