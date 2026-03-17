package com.example.afaq.utils.converters

import com.example.afaq.utils.localizeDigits

object TempConverter {

    fun convert(celsius: Double, unit: String): String {
        val converted = when (unit) {
            "Celsius °C" -> "${celsius.toInt()}°C"
            "Fahrenheit °F" -> "${celsiusToFahrenheit(celsius).toInt()}°F"
            "Kelvin °K" -> "${celsiusToKelvin(celsius).toInt()}K"
            else -> "${celsius.toInt()}°C"
        }
        return converted.localizeDigits()
    }

    private fun celsiusToFahrenheit(celsius: Double): Double = celsius * 9 / 5 + 32
    private fun celsiusToKelvin(celsius: Double): Double = celsius + 273.15
}