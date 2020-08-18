package com.example.ncov19traking

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ncov19traking.di.DaggerApplicationComponent
import java.util.concurrent.TimeUnit

class BaseApp : Application() {

    val applicationComponent by lazy { DaggerApplicationComponent.factory().create(this) }
    private var notificationChannel: NotificationChannel? = null
    private val workName = "WASH_HANDS"

    companion object {
        const val CHANNEL_ID = "covid channel id"
    }

    @RequiresApi(M)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showNotificationByWorker()
    }

    @RequiresApi(M)
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Covid-19 Tracking Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Provide reminders and info" }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationChannel?.let { notificationManager.createNotificationChannel(it) }
        }
    }

    private fun showNotificationByWorker() {
        val workManager = WorkManager.getInstance(this)
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(11, TimeUnit.HOURS).build()
        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }
}