package com.example.afaq.data.home.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.data.settings.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class WeatherDataStore(private val context: Context) {

    private val gson = Gson()

    suspend fun saveWeather(lat: Double, lon: Double, weather: Weather) {
        context.dataStore.edit {
            it[WeatherDataStoreKeys.weatherKey(lat, lon)] = gson.toJson(weather)
        }
    }

    suspend fun saveForecast(lat: Double, lon: Double, forecast: Forecast) {
        context.dataStore.edit {
            it[WeatherDataStoreKeys.forecastKey(lat, lon)] = gson.toJson(forecast)
        }
    }

    suspend fun getCachedWeather(lat: Double, lon: Double): Weather? {
        return context.dataStore.data
            .map { it[WeatherDataStoreKeys.weatherKey(lat, lon)] }
            .first()
            ?.let { gson.fromJson(it, Weather::class.java) }
    }

    suspend fun getCachedForecast(lat: Double, lon: Double): Forecast? {
        return context.dataStore.data
            .map { it[WeatherDataStoreKeys.forecastKey(lat, lon)] }
            .first()
            ?.let { gson.fromJson(it, Forecast::class.java) }
    }
}