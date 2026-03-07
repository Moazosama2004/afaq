package com.example.afaq.presentation.home.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.home.HomeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val homeRepo = HomeRepo()

    // states
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading : LiveData<Boolean> = _isLoading

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

//    private val _error = MutableLiveData<String>()
//    val error : LiveData<String> = _error

    init {
        getCurrentWeather(
            lat = 30.0444,
            lon = 31.2357,
            apiKey = "0dd33dbf9b7141bc610e6c76cabb1974",
            units = "metric",
            lang = "ar"
        )
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ) {
        _weatherState.value = WeatherUiState.Loading
        viewModelScope.launch {
            homeRepo.getCurrentWeather(
                lat = lat,
                lon = lon,
                apiKey = apiKey,
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
}