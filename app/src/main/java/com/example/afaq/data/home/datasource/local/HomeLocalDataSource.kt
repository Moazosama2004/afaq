package com.example.afaq.data.home.datasource.local

import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast

class HomeLocalDataSource(
    private val weatherDataStore: WeatherDataStore
) {

    suspend fun saveWeather(lat: Double, lon: Double, weather: Weather) {
        weatherDataStore.saveWeather(lat, lon, weather)
    }

    suspend fun saveForecast(lat: Double, lon: Double, forecast: Forecast) {
        weatherDataStore.saveForecast(lat, lon, forecast)
    }

    suspend fun getCachedWeather(lat: Double, lon: Double): Weather? {
        return weatherDataStore.getCachedWeather(lat, lon)
    }

    suspend fun getCachedForecast(lat: Double, lon: Double): Forecast? {
        return weatherDataStore.getCachedForecast(lat, lon)
    }
}