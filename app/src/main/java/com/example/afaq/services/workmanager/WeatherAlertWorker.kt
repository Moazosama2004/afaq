package com.example.afaq.services.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.network.RetroFitClient
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.services.notification.NotificationServiceImpl
import com.example.afaq.utils.Constants
import kotlinx.coroutines.flow.first

class WeatherAlertWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val endTime = inputData.getLong("EndTime", 0L)

        // check still within period
        if (System.currentTimeMillis() <= endTime) {
            try {
                val settingsRepo = SettingsRepo(context)
                val lat = settingsRepo.userLat.first() ?: Constants.DEFAULT_LAT
                val lon = settingsRepo.userLon.first() ?: Constants.DEFAULT_LON
                val lang = settingsRepo.language.first().take(2).lowercase()
                val units = if (settingsRepo.tempUnit.first().contains("Celsius")) "metric" else "imperial"

                val remoteDataSource = HomeRemoteDataSource(RetroFitClient.webApiService)
                val result = remoteDataSource.getCurrentWeather(lat, lon, units, lang)

                val message = if (result.isSuccess) {
                    val weather = result.getOrNull()
                    val temp = weather?.temperature?.toInt() ?: "--"
                    val desc = weather?.description ?: "Weather update"
                    val city = weather?.cityName ?: "your location"
                    "It's $temp° in $city. $desc. Stay updated! 🌤️"
                } else {
                    "Weather Alert! Check the weather now 🌤️"
                }

                NotificationServiceImpl(context).showNotification(message)
            } catch (e: Exception) {
                Log.e("WeatherAlertWorker", "Error fetching weather", e)
                NotificationServiceImpl(context).showNotification("Weather Alert! Check the weather now 🌤️")
            }
        }

        return Result.success()
    }
}