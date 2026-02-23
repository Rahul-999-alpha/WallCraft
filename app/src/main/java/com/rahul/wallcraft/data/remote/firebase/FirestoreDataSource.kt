package com.rahul.wallcraft.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rahul.wallcraft.domain.model.Category
import com.rahul.wallcraft.domain.model.Wallpaper
import com.rahul.wallcraft.domain.model.WallpaperSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getEditorPicks(limit: Long = 20): List<Wallpaper> = try {
        val snapshot = firestore.collection("editor_picks")
            .orderBy("order", Query.Direction.ASCENDING)
            .limit(limit)
            .get()
            .await()

        snapshot.documents.mapNotNull { doc ->
            doc.toWallpaper()
        }
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getCuratedWallpapers(
        category: String? = null,
        limit: Long = 20
    ): List<Wallpaper> = try {
        var query: Query = firestore.collection("curated_wallpapers")
        if (category != null) {
            query = query.whereEqualTo("category", category)
        }
        val snapshot = query.limit(limit).get().await()
        snapshot.documents.mapNotNull { it.toWallpaper() }
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun getCategories(): List<Category> = try {
        val snapshot = firestore.collection("categories")
            .orderBy("order", Query.Direction.ASCENDING)
            .get()
            .await()

        snapshot.documents.mapNotNull { doc ->
            Category(
                id = doc.id,
                name = doc.getString("name") ?: return@mapNotNull null,
                displayName = doc.getString("displayName") ?: doc.getString("name") ?: return@mapNotNull null,
                thumbnailUrl = doc.getString("thumbnailUrl"),
                wallpaperCount = doc.getLong("count")?.toInt() ?: 0,
                isPinned = doc.getBoolean("isPinned") ?: false
            )
        }
    } catch (e: Exception) {
        emptyList()
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toWallpaper(): Wallpaper? {
        return Wallpaper(
            id = "${WallpaperSource.CURATED_FIREBASE.prefix}_$id",
            source = WallpaperSource.CURATED_FIREBASE,
            title = getString("title") ?: "Wallpaper",
            thumbnailUrl = getString("thumbnailUrl") ?: return null,
            previewUrl = getString("previewUrl") ?: getString("thumbnailUrl") ?: return null,
            fullUrl = getString("fullUrl") ?: return null,
            width = getLong("width")?.toInt() ?: 1080,
            height = getLong("height")?.toInt() ?: 1920,
            dominantColor = getString("dominantColor"),
            tags = (get("tags") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            category = getString("category"),
            isAmoled = getBoolean("isAmoled") ?: false
        )
    }
}
