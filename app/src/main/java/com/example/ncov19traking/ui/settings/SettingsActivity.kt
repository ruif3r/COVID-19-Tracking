package com.example.ncov19traking.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.ncov19traking.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val nightMode = "NightMode"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nightModeSwitch = findViewById<SwitchMaterial>(R.id.night_mode_switch)
        checkSwitchStatus(nightModeSwitch, preferences)
        nightModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            preferences.edit().run {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    putBoolean(nightMode, true)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    putBoolean(nightMode, false)
                }
                apply()
            }
        }
    }

    private fun checkSwitchStatus(switch: SwitchMaterial, preferences: SharedPreferences) {
        switch.isChecked = preferences.getBoolean(nightMode, false)
    }
}