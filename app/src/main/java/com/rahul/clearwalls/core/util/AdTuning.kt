package com.rahul.clearwalls.core.util

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Server-tunable ad-frequency knobs via Firebase Remote Config — replaces both
 * the hardcoded constants and the deleted admin panel. Change values fleet-wide
 * from Firebase console -> Remote Config using the KEY_* parameter names; the
 * in-code defaults apply until an override is fetched (12h default fetch cadence,
 * plus app cold start).
 *
 * Safety: every getter falls back to its default when the key is unset (0) and
 * floor-clamps the result, so a console typo can never produce an ad-storm.
 */
object AdTuning {
    const val KEY_INLINE_INTERVAL = "ad_inline_interval"
    const val KEY_INTERSTITIAL_SET_INTERVAL = "ad_interstitial_set_interval"
    const val KEY_INTERSTITIAL_DOWNLOAD_INTERVAL = "ad_interstitial_download_interval"
    const val KEY_INTERSTITIAL_COOLDOWN_SEC = "ad_interstitial_cooldown_sec"
    const val KEY_FIRST_SESSION_GRACE_SEC = "ad_first_session_grace_sec"

    private const val DEFAULT_INLINE_INTERVAL = 8L            // native card every N grid items
    private const val DEFAULT_SET_INTERVAL = 4L               // interstitial every Nth set action
    private const val DEFAULT_DOWNLOAD_INTERVAL = 4L          // interstitial every Nth download
    private const val DEFAULT_COOLDOWN_SEC = 180L             // min gap between interstitials
    private const val DEFAULT_GRACE_SEC = 600L                // ad-free first session window

    /** Registered at app start via setDefaultsAsync. */
    val DEFAULTS: Map<String, Any> = mapOf(
        KEY_INLINE_INTERVAL to DEFAULT_INLINE_INTERVAL,
        KEY_INTERSTITIAL_SET_INTERVAL to DEFAULT_SET_INTERVAL,
        KEY_INTERSTITIAL_DOWNLOAD_INTERVAL to DEFAULT_DOWNLOAD_INTERVAL,
        KEY_INTERSTITIAL_COOLDOWN_SEC to DEFAULT_COOLDOWN_SEC,
        KEY_FIRST_SESSION_GRACE_SEC to DEFAULT_GRACE_SEC,
    )

    /**
     * Unset keys read as 0 from Remote Config (also true in the brief window
     * before async defaults apply) — treat 0 as "use default", then floor-clamp.
     */
    internal fun resolve(raw: Long, default: Long, floor: Long): Long =
        (if (raw <= 0L) default else raw).coerceAtLeast(floor)

    private val rc: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    val inlineInterval: Int
        get() = resolve(rc.getLong(KEY_INLINE_INTERVAL), DEFAULT_INLINE_INTERVAL, floor = 4L).toInt()

    val interstitialSetInterval: Int
        get() = resolve(rc.getLong(KEY_INTERSTITIAL_SET_INTERVAL), DEFAULT_SET_INTERVAL, floor = 2L).toInt()

    val interstitialDownloadInterval: Int
        get() = resolve(rc.getLong(KEY_INTERSTITIAL_DOWNLOAD_INTERVAL), DEFAULT_DOWNLOAD_INTERVAL, floor = 2L).toInt()

    val interstitialCooldownMs: Long
        get() = resolve(rc.getLong(KEY_INTERSTITIAL_COOLDOWN_SEC), DEFAULT_COOLDOWN_SEC, floor = 60L) * 1000L

    val firstSessionGraceMs: Long
        get() = resolve(rc.getLong(KEY_FIRST_SESSION_GRACE_SEC), DEFAULT_GRACE_SEC, floor = 120L) * 1000L
}
