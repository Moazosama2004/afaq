package com.example.afaq.utils

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
