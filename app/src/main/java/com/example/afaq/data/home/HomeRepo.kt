package com.example.afaq.data.home

import android.content.Context
import com.example.afaq.data.home.datasource.local.WeatherDataStore
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.utils.NetworkUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class HomeRepo(
    private val remoteDataSource: HomeRemoteDataSource,
    private val weatherDataStore: WeatherDataStore,
    private val context: Context
) {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Result<Weather> {
        return (if (NetworkUtils.isOnline(context)) {
            // online -> fetch from API → save to DataStore
            val result = remoteDataSource.getCurrentWeather(lat, lon,apiKey, units, lang)
            result.onSuccess { weather ->
                weatherDataStore.saveWeather(weather)
            }
            result
        } else {
            // offline → load from DataStore
            val cached = weatherDataStore.cachedWeather
                .map { it }
                .first()

            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("No internet and no cached data"))
            }
        })
    }

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Result<Forecast> {
        return if (NetworkUtils.isOnline(context)) {
            //  online → fetch from API → save to DataStore
            val result = remoteDataSource.getForecast(lat, lon, apiKey,units, lang)
            result.onSuccess { forecast ->
                weatherDataStore.saveForecast(forecast)
            }
            result
        } else {
            //  offline → load from DataStore
            val cached = weatherDataStore.cachedForecast
                .map { it }
                .first()

            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("No internet and no cached data"))
            }
        }
    }
}