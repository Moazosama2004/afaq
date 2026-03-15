package com.example.afaq.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.example.afaq.data.location.LocationRepository
import com.example.afaq.presentation.settings.manager.SettingsViewModel
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─── Dynamic Locale ───────────────────────────────────────
fun getAppLocale(): Locale = Locale.getDefault()
// ← automatically uses current app locale (set by LocaleHelper)

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEE dd, M, yyyy", getAppLocale())
    return sdf.format(Date(timestamp * 1000))
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", getAppLocale())
    return sdf.format(Date(timestamp * 1000))
}

fun formatForecastDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEE dd.M", getAppLocale())
    return sdf.format(Date(timestamp * 1000))
}

fun formatHour(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", getAppLocale())
    return sdf.format(Date(timestamp * 1000))
}

fun isSameDay(timestamp: Long): Boolean {
    val fmt = SimpleDateFormat("yyyy-MM-dd", getAppLocale())
    val today = fmt.format(Date())
    val itemDay = fmt.format(Date(timestamp * 1000))
    return today == itemDay
}

fun getDayName(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE", getAppLocale())
    return sdf.format(Date(timestamp * 1000))
}

fun formatAlertTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", getAppLocale())
    return sdf.format(Date(timestamp))
}

suspend fun fetchGpsLocation(
    context: Context,
    settingsViewModel: SettingsViewModel
) {
    try {
        val locationRepository = LocationRepository(context)
        withTimeoutOrNull(10000L) {
            locationRepository.getUserLocation().collect { (lat, lon) ->
                Log.d("Settings", "✅ GPS location: $lat, $lon")
                settingsViewModel.saveUserLocation(lat, lon)
            }
        } ?: run {
            // timeout → keep current location
            Log.d("Settings", "⚠️ GPS timeout - keeping current location")
        }
    } catch (e: Exception) {
        Log.d("Settings", "❌ GPS error: ${e.message}")
    }
}

fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
    } catch (e: Exception) {
        "Unknown Location"
    }
}
