package com.example.ncov19traking

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

const val NOTIFICATION_ID = 101

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        val intent = getMainActivityIntent()
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val builder = reminderNotificationBuilder(pendingIntent)
        showNotification(builder)
        return Result.success()
    }

    private fun showNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun getMainActivityIntent() = Intent(
            applicationContext,
            MainActivity::class.java
        ).apply { Intent.FLAG_ACTIVITY_CLEAR_TASK }

    private fun reminderNotificationBuilder(pendingIntent: PendingIntent?): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, BaseApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Reminder")
            .setContentText("Remember to wash your hands for at least 20 seconds!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Remember to wash your hands for at least 20 seconds!")
            ).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

}