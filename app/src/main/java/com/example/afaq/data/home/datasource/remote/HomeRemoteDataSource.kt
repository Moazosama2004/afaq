package com.example.afaq.data.home.datasource.remote

import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.home.model.toForecast
import com.example.afaq.data.home.model.toWeather
import com.example.afaq.data.model.Forecast

class HomeRemoteDataSource(
    private val weatherApiService: WeatherApiService
) {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<Weather> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                lat,
                lon,
                units,
                lang
            )
            if (response.isSuccessful) {
                val weather = response.body()?.toWeather()
                if (weather != null) {
                    Result.success(weather)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(
                    Exception("Can't load Weather Data ${response.code()}, ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<Forecast> {
        return try {
            val response = weatherApiService.getForecast(
                lat = lat,
                lon = lon,
                units = units,
                lang = lang
            )
            if (response.isSuccessful) {
                val forecast = response.body()?.toForecast()
                if (forecast != null) {
                    Result.success(forecast)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(
                    Exception("Can't load Forecast ${response.code()}, ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}