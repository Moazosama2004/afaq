package com.example.afaq.presentation.home.manager

import com.example.afaq.data.home.model.Weather

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}