package com.example.afaq.data.home.datasource.remote

import com.example.afaq.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): Response<CurrentWeatherDto>

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): Response<ForecastDto>
}