package com.example.morethanyesterday.settings

import android.content.Context
import android.content.SharedPreferences

class SettingsSharedPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, 0)

    var notiEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTI_TOGGLE, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTI_TOGGLE, value).apply()

    var notiHour: Int
        get() = prefs.getInt(KEY_NOTI_HOUR, 9)
        set(value) = prefs.edit().putInt(KEY_NOTI_HOUR, value).apply()

    var notiMinute: Int
        get() = prefs.getInt(KEY_NOTI_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_NOTI_MINUTE, value).apply()

    companion object {
        const val PREF_NAME = "SETTINGS_PREFERENCE"

        const val KEY_NOTI_TOGGLE = "KEY_NOTI_TOGGLE"
        const val KEY_NOTI_HOUR = "KEY_NOTI_HOUR"
        const val KEY_NOTI_MINUTE = "KEY_NOTI_MINUTE"
    }
}