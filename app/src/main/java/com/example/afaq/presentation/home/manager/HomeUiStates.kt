package com.example.afaq.presentation.home.manager

import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast


sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data class Success(val forecast: Forecast) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}