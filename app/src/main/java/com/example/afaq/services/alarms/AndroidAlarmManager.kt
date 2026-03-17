package com.example.afaq.services.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.services.recievers.AlarmReceiver

class AndroidAlarmManager(
    private val context: Context
) : IAlarmService {

    // 1. create instance from alarm manager
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlertEntity) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("Message", "Weather Alert! Check the weather now 🌤️")
            putExtra("AlertId", item.id)
            putExtra("EndTime", item.endTime)
            putExtra("Type", item.type)
        }


        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.startTime,
            PendingIntent.getBroadcast(
                context,
                item.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: AlertEntity) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id, // ← same id as schedule
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}