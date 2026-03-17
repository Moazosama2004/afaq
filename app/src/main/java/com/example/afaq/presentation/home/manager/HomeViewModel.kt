package com.example.afaq.presentation.home.manager


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.home.HomeRepo
import com.example.afaq.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepo: HomeRepo
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    private val _forecastState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val forecastState: StateFlow<ForecastUiState> = _forecastState

    fun loadWeather(lat: Double, lon: Double, lang: String = "en", units: String = "metric") {
        getCurrentWeather(lat = lat, lon = lon, lang = lang, units = units)
        getForecast(lat = lat, lon = lon, lang = lang, units = units)
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ) {
        _weatherState.value = WeatherUiState.Loading
        viewModelScope.launch {
            homeRepo.getCurrentWeather(
                lat = lat,
                lon = lon,
                units = units,
                lang = lang
            ).onSuccess { weather ->
                _weatherState.value = WeatherUiState.Success(weather)
            }.onFailure { error ->
                _weatherState.value = WeatherUiState.Error(
                    error.message ?: "Unknown error"
                )
            }
        }
    }

    fun getForecast(
        lat: Double,
        lon: Double,
        units: String = "metric",
        lang: String = "en"
    ) {
        _forecastState.value = ForecastUiState.Loading
        viewModelScope.launch {
            homeRepo.getForecast(
                lat = lat,
                lon = lon,
                units = units,
                lang = lang
            ).onSuccess { forecast ->
                _forecastState.value = ForecastUiState.Success(forecast)
            }.onFailure { error ->
                _forecastState.value = ForecastUiState.Error(
                    error.message ?: "Unknown error"
                )
            }
        }
    }
}


class HomeViewModelFactory(
    private val homeRepo: HomeRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(homeRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}