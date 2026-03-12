package com.example.afaq.services.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.afaq.services.notification.NotificationServiceImpl

class WeatherAlertWorker(
    private val context : Context,
    workerParameters: WorkerParameters
): Worker(context , workerParameters){

    override fun doWork(): Result {
        val message = inputData.getString("Message") ?: "Weather Alert!"
        val endTime = inputData.getLong("EndTime", 0L)
        val type = inputData.getString("Type") ?: "NOTIFICATION"

        // check still within period
        if (System.currentTimeMillis() <= endTime) {
                NotificationServiceImpl(context).showNotification(message)
        }

        return Result.success()
    }
}