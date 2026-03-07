package com.example.afaq.data.home.datasource.remote

import com.example.afaq.data.home.model.Weather
import com.example.afaq.network.RetroFitClient

class HomeRemoteDataSource {
    private val weatherApiService: WeatherApiService = RetroFitClient.webApiService

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Result<Weather> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                lat,
                lon,
                apiKey,
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
}