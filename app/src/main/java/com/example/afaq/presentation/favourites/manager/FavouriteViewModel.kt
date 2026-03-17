package com.example.afaq.presentation.favourites.manager

import AddFavouriteState
import DeleteFavouriteState
import FavouritesUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.favourite.FavouriteRepo
import com.example.afaq.data.local.db.FavouriteEntity
import com.example.afaq.data.local.db.toFavouriteEntity
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

    private val _entityToDelete = MutableStateFlow<FavouriteEntity?>(null)
    val entityToDelete: StateFlow<FavouriteEntity?> = _entityToDelete

    private val _showOfflineDialog = MutableStateFlow<Boolean>(false)
    val showOfflineDialog: StateFlow<Boolean> = _showOfflineDialog

    private val _showMap = MutableStateFlow(false)
    val showMap: StateFlow<Boolean> = _showMap

    private val _lat = MutableStateFlow(0.0)
    val lat: StateFlow<Double> = _lat

    private val _lon = MutableStateFlow(0.0)
    val lon: StateFlow<Double> = _lon

    private val _isLocationSelected = MutableStateFlow(false)
    val isLocationSelected: StateFlow<Boolean> = _isLocationSelected

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
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

    fun setShowMap(show: Boolean) {
        _showMap.value = show
        if (!show) {
            _isLocationSelected.value = false
        }
    }

    fun setLocation(latitude: Double, longitude: Double) {
        _lat.value = latitude
        _lon.value = longitude
        _isLocationSelected.value = true
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
        _addState.value = AddFavouriteState.Loading
        viewModelScope.launch {
            val result = repo.getCurrentWeather(
                lat = lat,
                lon = lon,
                units = "metric",
                lang = lang
            )

            result.onSuccess { weather ->
                repo.insertFavourite(weather.toFavouriteEntity())
                _addState.value = AddFavouriteState.Success
                setShowMap(false)
            }.onFailure { e ->
                _addState.value = AddFavouriteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteFavouriteById(id: Int) {
        _deleteState.value = DeleteFavouriteState.Loading
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

    fun showDialog(entity: FavouriteEntity) {
        _entityToDelete.value = entity
    }

    fun hideDialog() {
        _entityToDelete.value = null
    }

    fun confirmDelete() {
        val entity = _entityToDelete.value ?: return
        deleteFavouriteById(entity.id)
        hideDialog()
    }

    fun showOfflineDialog() {
        _showOfflineDialog.value = true
    }

    fun hideOfflineDialog() {
        _showOfflineDialog.value = false
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
