package com.example.afaq.data.home

import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.home.model.Weather

class HomeRepo {
    private val homeRemoteDataSource = HomeRemoteDataSource()

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ) : Result<Weather> {
       return homeRemoteDataSource.getCurrentWeather(
            lat,
            lon,
            apiKey,
            units,
            lang
        )
    }
}