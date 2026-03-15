package com.example.afaq.services.recievers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.network.RetroFitClient
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.services.notification.NotificationServiceImpl
import com.example.afaq.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

        val endTime = intent.getLongExtra("EndTime", 0L)
        val type = intent.getStringExtra("Type") ?: "NOTIFICATION"

        if (System.currentTimeMillis() <= endTime) {
            val pendingResult = goAsync()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    val settingsRepo = SettingsRepo(context)
                    val lat = settingsRepo.userLat.first() ?: Constants.DEFAULT_LAT
                    val lon = settingsRepo.userLon.first() ?: Constants.DEFAULT_LON
                    val lang = settingsRepo.language.first().take(2).lowercase()
                    val units = if (settingsRepo.tempUnit.first().contains("Celsius")) "metric" else "imperial"

                    val remoteDataSource = HomeRemoteDataSource(RetroFitClient.webApiService)
                    val result = remoteDataSource.getCurrentWeather(lat, lon, Constants.API_KEY, units, lang)

                    val message = if (result.isSuccess) {
                        val weather = result.getOrNull()
                        val temp = weather?.temperature?.toInt() ?: "--"
                        val desc = weather?.description ?: "Weather update"
                        val city = weather?.cityName ?: "your location"
                        "It's $temp° in $city. $desc. Have a great day! 🌤️"
                    } else {
                        "Weather Alert! Check the weather now 🌤️"
                    }

                    val notificationService = NotificationServiceImpl(context)
                    if (type == "ALARM") {
                        notificationService.showAlarm(message)
                    } else {
                        notificationService.showNotification(message)
                    }
                } catch (e: Exception) {
                    Log.e("AlarmReciever", "Error fetching weather", e)
                    val notificationService = NotificationServiceImpl(context)
                    if (type == "ALARM") {
                        notificationService.showAlarm("Weather Alert! Check the weather now 🌤️")
                    } else {
                        notificationService.showNotification("Weather Alert! Check the weather now 🌤️")
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}