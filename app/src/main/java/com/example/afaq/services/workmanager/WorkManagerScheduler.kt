package com.example.afaq.services.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.afaq.data.alarm.model.AlertEntity
import java.util.concurrent.TimeUnit

class WorkManagerScheduler(
    private val context: Context

) {
    fun schedule(alert: AlertEntity) {
        val delay = alert.startTime - System.currentTimeMillis()


        if (delay <= 0) return

        val inputData = workDataOf(
            "Message" to "Weather Alert! Check the weather now 🌤️\nfrom WorkManager",
            "EndTime" to alert.endTime,
            "Type" to alert.type
        )

        val workRequest = PeriodicWorkRequestBuilder<WeatherAlertWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("alert_${alert.id}")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "alert_${alert.id}",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancel(alertId: Int) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("alert_${alertId}")
    }
}