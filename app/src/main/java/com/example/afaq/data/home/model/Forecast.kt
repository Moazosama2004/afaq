package com.example.afaq.data.model

data class Forecast(
    val cityName: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val items: List<ForecastItem>
)

data class ForecastItem(
    val dt: Long,
    val dtTxt: String,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val visibility: Int,
    val description: String,
    val icon: String,
    val pop: Double        // rain probability 0.0 - 1.0
)
