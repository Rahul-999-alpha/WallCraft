package com.rahul.clearwalls.core.util

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.rahul.clearwalls.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wraps Google's User Messaging Platform (the certified CMP AdMob requires for
 * EEA/UK traffic since Jan 2024). Ads must only be requested once
 * [canRequestAds] is true; see MainActivity for the gather-then-init flow.
 */
@Singleton
class ConsentManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    /** True when consent state allows ad requests (includes "not required" regions). */
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /** True when the region requires a re-openable privacy options entry point. */
    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    /**
     * Refreshes consent info and shows the consent form if the region requires one.
     * [onConsentGathered] always fires (with the form/request error, if any) —
     * callers decide whether ads may start via [canRequestAds].
     */
    fun gatherConsent(activity: Activity, onConsentGathered: (FormError?) -> Unit) {
        val paramsBuilder = ConsentRequestParameters.Builder()

        if (BuildConfig.DEBUG) {
            // Force EEA geography on debug builds so the form is testable anywhere.
            paramsBuilder.setConsentDebugSettings(
                ConsentDebugSettings.Builder(activity)
                    .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                    .build()
            )
        }

        consentInformation.requestConsentInfoUpdate(
            activity,
            paramsBuilder.build(),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    onConsentGathered(formError)
                }
            },
            { requestError -> onConsentGathered(requestError) }
        )
    }

    /** Re-opens the consent form so users can change their choice (Settings entry). */
    fun showPrivacyOptionsForm(activity: Activity, onDismissed: (FormError?) -> Unit) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            onDismissed(formError)
        }
    }
}
