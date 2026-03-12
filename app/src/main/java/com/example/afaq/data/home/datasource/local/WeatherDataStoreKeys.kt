package com.example.afaq.data.home.datasource.local

import androidx.datastore.preferences.core.stringPreferencesKey

object WeatherDataStoreKeys {
    val CURRENT_WEATHER = stringPreferencesKey("current_weather")
    val FORECAST = stringPreferencesKey("forecast")
}