package com.rahul.clearwalls.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.rahul.clearwalls.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-app content reporting, required by Google Play's AI-Generated Content policy
 * (users must be able to flag offensive content without leaving the app). Reports
 * land in the Firestore `reports` collection; review them from the Firebase console
 * and use them to prune the catalog / extend the prompt blocklist.
 *
 * Firestore rules must allow `create` (and nothing else) on `reports` for clients —
 * see tools/seed_wallpapers/firestore.rules.
 */
@Singleton
class ReportRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "ReportRepository"
        const val TYPE_WALLPAPER = "wallpaper"
        const val TYPE_AI_GENERATION = "ai_generation"
    }

    /** Best-effort submit; returns false instead of throwing so UI can stay simple. */
    suspend fun submitReport(
        contentId: String,
        contentType: String,
        title: String,
        imageUrl: String,
        reason: String,
        prompt: String? = null
    ): Boolean = try {
        val report = hashMapOf(
            "contentId" to contentId,
            "contentType" to contentType,
            "title" to title,
            "imageUrl" to imageUrl,
            "reason" to reason,
            "prompt" to prompt,
            "appVersion" to BuildConfig.VERSION_NAME,
            "createdAt" to FieldValue.serverTimestamp()
        )
        firestore.collection("reports").add(report).await()
        Log.d(TAG, "Report submitted for $contentType $contentId ($reason)")
        true
    } catch (e: kotlinx.coroutines.CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w(TAG, "Report submission failed: ${e.message}")
        false
    }
}
