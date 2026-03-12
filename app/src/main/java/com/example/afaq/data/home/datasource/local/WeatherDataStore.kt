package com.example.afaq.data.home.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.data.settings.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class WeatherDataStore(private val context: Context) {

    private val gson = Gson()


    suspend fun saveWeather(weather: Weather) {
        context.dataStore.edit {
            it[WeatherDataStoreKeys.CURRENT_WEATHER] = gson.toJson(weather)
        }
    }

    suspend fun saveForecast(forecast: Forecast) {
        context.dataStore.edit {
            it[WeatherDataStoreKeys.FORECAST] = gson.toJson(forecast)
        }
    }


    val cachedWeather: Flow<Weather?> = context.dataStore.data
        .map { prefs ->
            prefs[WeatherDataStoreKeys.CURRENT_WEATHER]?.let {
                gson.fromJson(it, Weather::class.java)
            }
        }

    val cachedForecast: Flow<Forecast?> = context.dataStore.data
        .map { prefs ->
            prefs[WeatherDataStoreKeys.FORECAST]?.let {
                gson.fromJson(it, Forecast::class.java)
            }
        }
}