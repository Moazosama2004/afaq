package com.example.afaq.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.afaq.MainActivity
import com.example.afaq.R
import com.example.afaq.services.recievers.AlarmReceiver

class NotificationServiceImpl(
    private val context: Context
) : NotificationService {

    private val manager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager

    // ─── Show Notification ────────────────────────────────
    override fun showNotification(message: String) {
        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/raw/alert_sound"
        )
        val uniqueId = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            context,
            uniqueId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Weather Update 🌤️")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(NOTIFICATION_GROUP)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN) // ← each child alerts ✅
            .setOnlyAlertOnce(false)                                         // ← sound every time ✅
            .build()

        manager.notify(uniqueId, notification)
        showGroupSummary(CHANNEL_ID) // ← pass correct channel ✅
    }

    // ─── Show Alarm ───────────────────────────────────────
    override fun showAlarm(message: String, alertId: Int) {
        val soundUri = Uri.parse(
            "android.resource://${context.packageName}/raw/alert_sound"
        )

        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            alertId,
            Intent(context, AlarmReceiver::class.java).apply {
                action = "DISMISS_ALARM"
                putExtra("AlertId", alertId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            alertId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ALARM_CHANNEL_ID) // ← alarm channel ✅
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Afaq Weather Alarm 🚨")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setGroup(ALARM_GROUP)                                               // ← separate group ✅
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setOnlyAlertOnce(false)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                dismissPendingIntent
            )
            .build()

        manager.notify(alertId, notification)
        showGroupSummary(ALARM_CHANNEL_ID) // ← alarm channel for summary ✅
    }

    // ─── Group Summary ────────────────────────────────────
    private fun showGroupSummary(channelId: String) {
        val isAlarm = channelId == ALARM_CHANNEL_ID
        val group = if (isAlarm) ALARM_GROUP else NOTIFICATION_GROUP

        val summary = NotificationCompat.Builder(context, channelId) // ← correct channel ✅
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Afaq - آفاق")
            .setGroup(group)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true) // ← summary itself silent ✅
            .build()

        manager.notify(
            if (isAlarm) ALARM_SUMMARY_ID else NOTIFICATION_SUMMARY_ID,
            summary
        )
    }

    companion object {
        const val CHANNEL_ID = "afaq_channel"
        const val ALARM_CHANNEL_ID = "afaq_alarm_channel"
        const val NOTIFICATION_GROUP = "afaq_notification_group"
        const val ALARM_GROUP = "afaq_alarm_group"
        const val NOTIFICATION_SUMMARY_ID = 1000
        const val ALARM_SUMMARY_ID = 1001
    }
}
