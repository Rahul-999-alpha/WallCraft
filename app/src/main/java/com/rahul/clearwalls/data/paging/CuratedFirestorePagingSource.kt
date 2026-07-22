package com.rahul.clearwalls.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rahul.clearwalls.data.mapper.toCuratedWallpaper
import com.rahul.clearwalls.domain.model.Wallpaper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Pages the owned wallpaper catalog out of Firestore.
 *
 * The app's catalog lives in our own Firebase project (seeded via tools/seed_wallpapers)
 * instead of third-party photo APIs, whose terms prohibit wallpaper apps.
 *
 * Query shapes are restricted to ones Firestore serves WITHOUT composite indexes:
 * - equality filter (category) or array-contains (tags) with the implicit __name__ order
 * - editor picks ordered by the single "order" field
 * Pagination is cursor-based via startAfter(lastDocumentSnapshot).
 */
class CuratedFirestorePagingSource(
    private val firestore: FirebaseFirestore,
    private val category: String? = null,
    private val searchQuery: String? = null,
    private val editorPicksOnly: Boolean = false
) : PagingSource<DocumentSnapshot, Wallpaper>() {

    companion object {
        private const val TAG = "CuratedPagingSource"
        const val COLLECTION_CURATED = "curated_wallpapers"
        const val COLLECTION_EDITOR_PICKS = "editor_picks"

        /**
         * Reduces a free-text query to the first useful lowercase token so it can be
         * matched against the document's `tags` array (Firestore has no full-text search).
         */
        fun searchToken(raw: String): String? {
            val tokens = raw.trim().lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }
            return tokens.firstOrNull { it.length >= 3 } ?: tokens.firstOrNull()
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Wallpaper>): DocumentSnapshot? {
        // Cursor-based source: a refresh restarts from the top of the catalog.
        return null
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Wallpaper> {
        return try {
            var query: Query = if (editorPicksOnly) {
                firestore.collection(COLLECTION_EDITOR_PICKS)
                    .orderBy("order", Query.Direction.ASCENDING)
            } else {
                var q: Query = firestore.collection(COLLECTION_CURATED)
                category?.let { q = q.whereEqualTo("category", it) }
                searchQuery?.let { raw ->
                    searchToken(raw)?.let { token -> q = q.whereArrayContains("tags", token) }
                }
                q
            }

            params.key?.let { lastDoc -> query = query.startAfter(lastDoc) }

            val snapshot = query.limit(params.loadSize.toLong()).get().await()
            val wallpapers = snapshot.documents.mapNotNull { it.toCuratedWallpaper() }
            Log.d(TAG, "Loaded ${wallpapers.size} curated wallpapers " +
                    "(category=$category, search=$searchQuery, picks=$editorPicksOnly)")

            LoadResult.Page(
                data = wallpapers,
                prevKey = null,
                nextKey = if (snapshot.documents.size < params.loadSize) null
                          else snapshot.documents.lastOrNull()
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Firestore catalog load failed: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}
