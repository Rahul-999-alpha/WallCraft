package com.rahul.wallcraft.core.common

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun <T> Flow<T>.catchAndLog(): Flow<T> = catch { e ->
    android.util.Log.e("WallCraft", "Flow error: ${e.message}", e)
}
