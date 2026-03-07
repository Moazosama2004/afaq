package com.example.afaq.data.home.datasource.remote

import com.example.afaq.data.home.model.Weather

data class CurrentWeatherDto(
    val coord: Coord,
    val weather: List<WeatherX>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val sys: Sys,
    val dt: Int,
    val name: String
)

data class Coord(val lat: Double, val lon: Double)
data class WeatherX(val id: Int, val main: String, val description: String, val icon: String)
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(val speed: Double, val deg: Int)
data class Sys(val country: String?, val sunrise: Long, val sunset: Long)


// data/remote/dto/CurrentWeatherDto.kt (add at bottom)
fun CurrentWeatherDto.toWeather(): Weather {
    return Weather(
        cityName = name,
        country = sys.country ?: "",
        temperature = main.temp,
        feelsLike = main.feels_like,
        tempMin = main.temp_min,
        tempMax = main.temp_max,
        humidity = main.humidity,
        pressure = main.pressure,
        windSpeed = wind.speed,
        visibility = visibility,
        description = weather[0].description,
        icon = weather[0].icon,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        date = dt,
        lat = coord.lat,
        lon = coord.lon
    )
}