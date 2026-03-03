package com.rahul.clearwalls.presentation.components

import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.rahul.clearwalls.BuildConfig

private const val TAG = "NativeAdCard"

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            nativeAd?.destroy()
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AndroidView(
            factory = { context ->
                val adView = NativeAdView(context)

                val adLoader = AdLoader.Builder(context, BuildConfig.ADMOB_NATIVE_ID)
                    .forNativeAd { ad ->
                        Log.d(TAG, "✅ Native ad loaded: ${ad.headline}")
                        nativeAd?.destroy()
                        nativeAd = ad

                        adView.removeAllViews()

                        // Root layout
                        val rootLayout = LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        // Ad attribution label
                        val adLabel = TextView(context).apply {
                            text = "Ad"
                            textSize = 10f
                            setBackgroundColor(0xFFFFCC00.toInt())
                            setTextColor(0xFF000000.toInt())
                            setPadding(12, 4, 12, 4)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(16, 8, 0, 0)
                            }
                        }
                        rootLayout.addView(adLabel)

                        // Media view for ad image/video
                        val mediaView = MediaView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                400
                            ).apply {
                                setMargins(0, 8, 0, 0)
                            }
                            setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        }
                        rootLayout.addView(mediaView)
                        adView.mediaView = mediaView

                        // Headline
                        val headlineView = TextView(context).apply {
                            text = ad.headline ?: ""
                            textSize = 16f
                            setTextColor(0xFF212121.toInt())
                            maxLines = 2
                            setPadding(16, 12, 16, 0)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                        rootLayout.addView(headlineView)
                        adView.headlineView = headlineView

                        // Body text
                        if (ad.body != null) {
                            val bodyView = TextView(context).apply {
                                text = ad.body ?: ""
                                textSize = 13f
                                setTextColor(0xFF757575.toInt())
                                maxLines = 2
                                setPadding(16, 4, 16, 0)
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                            }
                            rootLayout.addView(bodyView)
                            adView.bodyView = bodyView
                        }

                        // Call to action button
                        if (ad.callToAction != null) {
                            val ctaButton = Button(context).apply {
                                text = ad.callToAction ?: "Learn More"
                                textSize = 14f
                                isAllCaps = false
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(16, 8, 16, 12)
                                }
                            }
                            rootLayout.addView(ctaButton)
                            adView.callToActionView = ctaButton
                        }

                        // Icon
                        if (ad.icon != null) {
                            val iconView = ImageView(context).apply {
                                setImageDrawable(ad.icon?.drawable)
                                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                                    gravity = Gravity.START
                                    setMargins(16, 8, 0, 0)
                                }
                            }
                            adView.iconView = iconView
                        }

                        adView.addView(rootLayout)
                        adView.setNativeAd(ad)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.e(TAG, "❌ Native ad failed: ${error.message} (code: ${error.code})")
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
                            .build()
                    )
                    .build()

                adLoader.loadAd(AdRequest.Builder().build())
                Log.d(TAG, "📢 Loading native ad...")
                adView
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )
    }
}
