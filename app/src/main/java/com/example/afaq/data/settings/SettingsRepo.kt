package com.example.afaq.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepo(
    private val context: Context
) {

    val language: Flow<String> = context.dataStore.data
        .map { it[SettingsKeys.LANGUAGE] ?: "English" }

    val tempUnit: Flow<String> = context.dataStore.data
        .map { it[SettingsKeys.TEMP_UNIT] ?: "Celsius °C" }

    val windUnit: Flow<String> = context.dataStore.data
        .map { it[SettingsKeys.WIND_UNIT] ?: "meter/sec" }

    val location: Flow<String> = context.dataStore.data
        .map { it[SettingsKeys.LOCATION] ?: "GPS" }

    val theme: Flow<String> = context.dataStore.data
        .map { it[SettingsKeys.THEME] ?: "System" }

    suspend fun setTheme(value: String) {
        context.dataStore.edit { it[SettingsKeys.THEME] = value }
    }

    suspend fun setLanguage(value: String) {
        context.dataStore.edit { it[SettingsKeys.LANGUAGE] = value }
    }

    suspend fun setTempUnit(value: String) {
        context.dataStore.edit { it[SettingsKeys.TEMP_UNIT] = value }
    }

    suspend fun setWindUnit(value: String) {
        context.dataStore.edit { it[SettingsKeys.WIND_UNIT] = value }
    }

    suspend fun setLocation(value: String) {
        context.dataStore.edit { it[SettingsKeys.LOCATION] = value }
    }


    suspend fun saveUserLocation(lat: Double, lon: Double) {
        context.dataStore.edit {
            it[SettingsKeys.USER_LAT] = lat.toString()
            it[SettingsKeys.USER_LON] = lon.toString()
        }
    }

    // Read user location
    val userLat: Flow<Double?> = context.dataStore.data
        .map { it[SettingsKeys.USER_LAT]?.toDoubleOrNull() }

    val userLon: Flow<Double?> = context.dataStore.data
        .map { it[SettingsKeys.USER_LON]?.toDoubleOrNull() }


}