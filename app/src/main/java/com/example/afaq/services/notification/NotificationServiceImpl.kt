package com.example.afaq.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.afaq.R
import com.example.afaq.services.recievers.AlarmReciever

class NotificationServiceImpl(
    private val context : Context
) : NotificationService{

    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    companion object {
        const val CHANNEL_ID = "afaq_channel"
        const val ALARM_NOTIFICATION_ID = 99
    }


    override fun showNotification(message: String) {
        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/raw/alert_sound"
        )
        val notification = NotificationCompat.Builder(context , CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Afaq - آفاق")
            .setContentText("$message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .build()

        manager.notify(1,notification)
    }

    override fun showAlarm(message: String) {
        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/raw/alert_sound"
        )

        // Dismiss intent
        val dismissIntent = Intent(context, AlarmReciever::class.java).apply {
            action = "DISMISS_ALARM"
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val dummyIntent = PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "afaq_alarm_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Afaq - آفاق 🌤️")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setFullScreenIntent(dummyIntent, true)
            .setOngoing(true)
            .setAutoCancel(false)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Dismiss",
                dismissPendingIntent
            )
            .build()

        manager.notify(ALARM_NOTIFICATION_ID, notification)
    }
}