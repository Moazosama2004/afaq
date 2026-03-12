package com.example.afaq.utils

object TempConverter {

    fun convert(celsius: Double, unit: String): String {
        return when (unit) {
            "Celsius °C"    -> "${celsius.toInt()}°C"
            "Fahrenheit °F" -> "${celsiusToFahrenheit(celsius).toInt()}°F"
            "Kelvin °K"     -> "${celsiusToKelvin(celsius).toInt()}K"
            else            -> "${celsius.toInt()}°C"
        }
    }

    fun celsiusToFahrenheit(celsius: Double): Double = celsius * 9 / 5 + 32
    fun celsiusToKelvin(celsius: Double): Double = celsius + 273.15
}