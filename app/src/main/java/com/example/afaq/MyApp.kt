package com.example.afaq

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        deleteOldChannels()
        createNotificationChannels()
    }


    private fun deleteOldChannels() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.deleteNotificationChannel("afaq_channel")
        manager.deleteNotificationChannel("afaq_alarm_channel")
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val soundUri = Uri.parse("android.resource://${packageName}/raw/alert_sound")
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val notificationChannel = NotificationChannel(
                "afaq_channel",
                "Afaq Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Weather notifications"
                setShowBadge(true)
                enableLights(true)
                enableVibration(true)
                setSound(
                    soundUri, AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }

            val alarmChannel = NotificationChannel(
                "afaq_alarm_channel",
                "Afaq Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Weather alarms"
                setSound(soundUri, audioAttributes)
                setBypassDnd(true)
                setShowBadge(true)
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
            manager.createNotificationChannel(alarmChannel)
        }
    }
}
