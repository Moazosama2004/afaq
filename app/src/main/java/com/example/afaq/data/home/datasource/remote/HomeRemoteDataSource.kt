package com.example.afaq.data.home.datasource.remote

import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.network.RetroFitClient

object HomeRemoteDataSource {
    private val weatherApiService: WeatherApiService = RetroFitClient.webApiService

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey : String,
        units: String = "metric",
        lang: String = "en"
    ): Result<Weather> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                lat,
                lon,
                apiKey = apiKey ,
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
        apiKey : String,
        units: String = "metric",
        lang: String = "en"
    ): Result<Forecast> {
        return try {
            val response = weatherApiService.getForecast(
                lat = lat,
                lon = lon,
                apiKey = apiKey,
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