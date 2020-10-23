package com.example.ncov19traking

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import com.example.ncov19traking.di.DaggerApplicationComponent
import java.util.concurrent.TimeUnit

class BaseApp : Application() {

    val applicationComponent by lazy { DaggerApplicationComponent.factory().create(this) }
    private var notificationChannel: NotificationChannel? = null
    private val workName = "WASH_HANDS"
    private val nightMode = "NightMode"

    companion object {
        const val CHANNEL_ID = "covid channel id"
        const val NOTIFICATION_REMINDER_TIME = 11L
    }

    @RequiresApi(M)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showNotificationByWorker()
        checkDayNightMode()
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
            PeriodicWorkRequestBuilder<NotificationWorker>(
                NOTIFICATION_REMINDER_TIME,
                TimeUnit.HOURS
            ).build()
        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }

    private fun checkDayNightMode() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean(nightMode, false)) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}