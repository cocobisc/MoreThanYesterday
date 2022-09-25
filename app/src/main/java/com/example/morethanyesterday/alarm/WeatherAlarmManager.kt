package com.example.morethanyesterday.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.morethanyesterday.alarm.receiver.AlarmReceiver
import java.util.*
import javax.inject.Inject

class WeatherAlarmManager @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
) {

    fun startAlarm(hour: Int, minute: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val target = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (target.before(current)) {
            target.add(Calendar.DATE, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            target.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

}