package com.example.ncov19traking

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.ncov19traking.di.DaggerApplicationComponent

class BaseApp : Application() {

    val applicationComponent by lazy { DaggerApplicationComponent.factory().create(this) }
    private var notificationChannel: NotificationChannel? = null
    private val nightMode = "NightMode"

    companion object {
        const val CHANNEL_ID = "covid channel id"
    }

    @RequiresApi(M)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        checkDayNightMode()
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

    private fun checkDayNightMode() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean(nightMode, false)) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}