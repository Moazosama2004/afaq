package com.example.afaq.data.home.model

import com.example.afaq.data.model.Forecast
import com.example.afaq.data.model.ForecastItem
import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("cod")
    val code: String,
    val message: Int,
    @SerializedName("cnt")
    val count: Int,
    val list: List<ForecastItemDto>,
    val city: CityDto
)

data class ForecastItemDto(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherX>,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @SerializedName("dt_txt")
    val dtTxt: String
)

data class CityDto(
    val name: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

fun ForecastDto.toForecast(): Forecast {
    return Forecast(
        cityName = city.name,
        country = city.country,
        sunrise = city.sunrise,
        sunset = city.sunset,
        items = list.map { item ->
            ForecastItem(
                dt = item.dt,
                dtTxt = item.dtTxt,
                temp = item.main.temp,
                feelsLike = item.main.feels_like,
                tempMin = item.main.temp_min,
                tempMax = item.main.temp_max,
                humidity = item.main.humidity,
                pressure = item.main.pressure,
                windSpeed = item.wind.speed,
                visibility = item.visibility,
                description = item.weather[0].description,
                icon = item.weather[0].icon,
                pop = item.pop
            )
        }
    )
}