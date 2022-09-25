package com.example.morethanyesterday.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.morethanyesterday.alarm.WeatherAlarmManager
import com.example.morethanyesterday.settings.SettingsSharedPreference

class DeviceBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val weatherAlarmManager = WeatherAlarmManager(context)
        val preferences = SettingsSharedPreference(context)

        if (preferences.notiEnabled) {
            weatherAlarmManager.cancelAlarm()
            weatherAlarmManager.startAlarm(preferences.notiHour, preferences.notiMinute)
        }
    }
}