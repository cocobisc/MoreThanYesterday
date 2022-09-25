package com.example.morethanyesterday.alarm.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.morethanyesterday.R
import com.example.morethanyesterday.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent?) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, getNotification(context))
    }

    private fun getNotification(context: Context): Notification {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java)
        val notifyPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("어제와오늘")
            .setContentText("어제와 오늘의 온도 차이를 확인하세요.")
            .setSmallIcon(R.mipmap.ic_main)
            .setContentIntent(notifyPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(false)
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "ID"
        const val CHANNEL_NAME = "NAME"
    }
}