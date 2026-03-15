package com.example.afaq.data.settings


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "afaq_settings")

object SettingsKeys {
    val LANGUAGE = stringPreferencesKey("language")       // "en" or "ar"
    val TEMP_UNIT = stringPreferencesKey("temp_unit")     // "Celsius" "Kelvin" "Fahrenheit"
    val WIND_UNIT = stringPreferencesKey("wind_unit")     // "meter/sec" "mile/hour"
    val LOCATION = stringPreferencesKey("location")       // "GPS" "Map"
    val USER_LAT = stringPreferencesKey("user_lat")
    val USER_LON = stringPreferencesKey("user_lon")
}