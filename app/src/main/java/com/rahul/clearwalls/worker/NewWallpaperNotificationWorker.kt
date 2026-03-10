package com.rahul.clearwalls.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rahul.clearwalls.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewWallpaperNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val channelId = "new_wallpapers"
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "New Wallpapers",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications about new wallpapers"
        }
        nm.createNotificationChannel(channel)

        val messages = listOf(
            "Fresh wallpapers just dropped! Check them out.",
            "New stunning wallpapers are waiting for you!",
            "Time to refresh your screen with new wallpapers!",
            "Discover today's new wallpaper collection!"
        )

        val intent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("ClearWalls")
            .setContentText(messages.random())
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        nm.notify(1001, notification)
        return Result.success()
    }
}
