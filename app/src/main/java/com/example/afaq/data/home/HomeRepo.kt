package com.example.afaq.data.home

import android.content.Context
import android.util.Log
import com.example.afaq.data.home.datasource.local.HomeLocalDataSource
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.utils.NetworkUtils

class HomeRepo(
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource,
    private val context: Context
) {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Result<Weather> {
        Log.d(
            "HomeRepo",
            "📡 getCurrentWeather: lat=$lat, lon=$lon online=${NetworkUtils.isOnline(context)}"
        )

        return if (NetworkUtils.isOnline(context)) {
            val result = remoteDataSource.getCurrentWeather(lat, lon, apiKey, units, lang)
            result.map { weather ->
                val weatherWithTime = weather.copy(
                    lastUpdated = System.currentTimeMillis()
                )
                Log.d("HomeRepo", "✅ API returned city: ${weatherWithTime.cityName}")
                localDataSource.saveWeather(lat, lon, weatherWithTime)
                weatherWithTime
            }
        } else {
            Log.d("HomeRepo", "📦 Loading cache for: lat=$lat, lon=$lon")
            val cached = localDataSource.getCachedWeather(lat, lon)
            Log.d("HomeRepo", "📦 Cache result: ${cached?.cityName}")
            if (cached != null) Result.success(cached)
            else Result.failure(Exception("No internet and no cached data"))
        }
    }

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Result<Forecast> {
        Log.d(
            "HomeRepo",
            "📡 getForecast: lat=$lat, lon=$lon online=${NetworkUtils.isOnline(context)}"
        )

        return if (NetworkUtils.isOnline(context)) {
            val result = remoteDataSource.getForecast(lat, lon, apiKey, units, lang)
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