package com.example.afaq.data.home.datasource.local

import androidx.datastore.preferences.core.stringPreferencesKey

object WeatherDataStoreKeys {
    fun weatherKey(lat: Double, lon: Double) =
        stringPreferencesKey("weather_${lat}_${lon}")

    fun forecastKey(lat: Double, lon: Double) =
        stringPreferencesKey("forecast_${lat}_${lon}")
}