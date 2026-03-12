package com.example.afaq.services.recievers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.afaq.services.notification.NotificationServiceImpl

class AlarmReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // handle dismiss action
        if (intent.action == "DISMISS_ALARM") {
            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            manager.cancel(NotificationServiceImpl.ALARM_NOTIFICATION_ID)
            return
        }

        val message = intent.getStringExtra("Message") ?: "Weather Alert!"
        val endTime = intent.getLongExtra("EndTime", 0L)
        val type = intent.getStringExtra("Type") ?: "NOTIFICATION"

        if (System.currentTimeMillis() <= endTime) {
            val notificationService = NotificationServiceImpl(context)

            if (type == "ALARM") {
                notificationService.showAlarm(message)
            } else {
                notificationService.showNotification(message)
            }
        }
    }
}