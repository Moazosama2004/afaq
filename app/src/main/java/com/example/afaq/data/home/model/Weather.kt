package com.example.afaq.data.home.model


data class Weather(
    val cityName: String,
    val country: String = "",
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val visibility: Int,
    val description: String,
    val icon: String,
    val sunrise: Long,
    val sunset: Long,
    val date: Int,
    val lat: Double,
    val lon: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
