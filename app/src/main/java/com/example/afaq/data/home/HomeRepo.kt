package com.example.afaq.data.home

import com.example.afaq.data.home.datasource.local.HomeLocalDataSource
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.data.network.INetworkUtils

class HomeRepo(
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource,
    private val networkUtils: INetworkUtils,
) {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Result<Weather> {
        return if (networkUtils.isOnline()) {
            val result = remoteDataSource.getCurrentWeather(lat, lon, units, lang)
            result.map { weather ->
                val weatherWithTime = weather.copy(
                    lastUpdated = System.currentTimeMillis()
                )
                localDataSource.saveWeather(lat, lon, weatherWithTime)
                weatherWithTime
            }
        } else {
            val cached = localDataSource.getCachedWeather(lat, lon)
            if (cached != null) Result.success(cached)
            else Result.failure(Exception("No internet and no cached data"))
        }
    }

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Result<Forecast> {
        return if (networkUtils.isOnline()) {
            val result = remoteDataSource.getForecast(lat, lon, units, lang)
            result.onSuccess { forecast ->
                localDataSource.saveForecast(lat, lon, forecast)
            }
            result
        } else {
            val cached = localDataSource.getCachedForecast(lat, lon)
            if (cached != null) Result.success(cached)
            else Result.failure(Exception("No internet and no cached data"))
        }
    }
}