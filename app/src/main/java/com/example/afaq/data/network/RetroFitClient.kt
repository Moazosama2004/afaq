package com.example.afaq.data.network

import com.example.afaq.data.home.datasource.remote.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    // Services
    val webApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)

    }
}