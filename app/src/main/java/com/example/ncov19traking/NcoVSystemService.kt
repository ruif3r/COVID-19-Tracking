package com.example.ncov19traking

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.ncov19traking.ui.global.GlobalFragment

class NcoVSystemService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceIntent = Intent(this, GlobalFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, serviceIntent, 0)
        val notification = NotificationCompat.Builder(this, NotificationChannelBuilder.CHANNEL_ID)
            .setContentTitle("Reminder")
            .setContentText("Wash your hands for at least 20 seconds!")
            .setSmallIcon(R.drawable.ic_timeline_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}