package com.example.ncov19traking

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi

class NotificationChannelBuilder : Application() {

    var notificationChannel: NotificationChannel? = null

    companion object {
        const val CHANNEL_ID = "covid channel id"
    }

    @RequiresApi(M)
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    @RequiresApi(M)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Covid-19 Tracking Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationChannel?.let { notificationManager.createNotificationChannel(it) }
        }


    }
}