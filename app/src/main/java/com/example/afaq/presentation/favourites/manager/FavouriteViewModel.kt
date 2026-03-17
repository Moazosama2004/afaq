package com.example.afaq.presentation.favourites.manager

import FavouritesUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.favourite.FavouriteRepo
import com.example.afaq.data.local.db.toFavouriteEntity
import com.example.afaq.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repo: FavouriteRepo
) : ViewModel() {

    private val _favouritesState = MutableStateFlow<FavouritesUiState>(FavouritesUiState.Loading)
    val favouritesState: StateFlow<FavouritesUiState> = _favouritesState

    private val _addState = MutableStateFlow<AddFavouriteState>(AddFavouriteState.Idle)
    val addState: StateFlow<AddFavouriteState> = _addState

    private val _deleteState = MutableStateFlow<DeleteFavouriteState>(DeleteFavouriteState.Idle)
    val deleteState: StateFlow<DeleteFavouriteState> = _deleteState

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        Log.d("FavouriteViewModel : ", "loadFavourites")
        viewModelScope.launch {
            repo.getAllFavourites()
                .catch { e ->
                    _favouritesState.value = FavouritesUiState.Error(e.message ?: "Unknown error")
                }
                .collect { list ->
                    _favouritesState.value = if (list.isEmpty()) {
                        FavouritesUiState.Empty
                    } else {
                        FavouritesUiState.Success(list)
                    }
                }
        }
    }

    fun refreshFavouritesWithLanguage(lang: String) {
        viewModelScope.launch {
            val currentList = repo.getAllFavourites().first()
            currentList.forEach { fav ->
                repo.getCurrentWeather(
                    lat = fav.lat,
                    lon = fav.lon,
                    units = "metric",
                    lang = lang
                ).onSuccess { weather ->
                    repo.insertFavourite(weather.toFavouriteEntity().copy(id = fav.id))
                }
            }
        }
    }

    fun addFavourite(lat: Double, lon: Double, lang: String) {
        Log.d("FavouriteViewModel : ", "addFavourite : $lat , $lon , $lang")
        _addState.value = AddFavouriteState.Loading
        viewModelScope.launch {
            // 1 - fetch weather from API
            val result = repo.getCurrentWeather(
                lat = lat,
                lon = lon,
                units = "metric",
                lang = lang
            )

            result.onSuccess { weather ->
                // 2 - save to Room
                repo.insertFavourite(weather.toFavouriteEntity())
                _addState.value = AddFavouriteState.Success
            }.onFailure { e ->
                _addState.value = AddFavouriteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteFavouriteById(id: Int) {
        _deleteState.value = DeleteFavouriteState.Loading
        Log.d("FavouriteViewModel : ", "deleteFavourite")
        viewModelScope.launch {
            runCatching {
                repo.deleteFavouriteById(id)
            }.onSuccess {
                _deleteState.value = DeleteFavouriteState.Success
                _deleteState.value = DeleteFavouriteState.Idle
            }.onFailure { e ->
                _deleteState.value = DeleteFavouriteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetAddState() {
        _addState.value = AddFavouriteState.Idle
    }
}


class FavouriteViewModelFactory(
    private val repo: FavouriteRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}