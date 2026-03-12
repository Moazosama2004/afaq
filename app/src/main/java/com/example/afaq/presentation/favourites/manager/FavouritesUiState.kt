import com.example.afaq.data.local.db.FavouriteEntity

sealed class FavouritesUiState {
    object Loading : FavouritesUiState()
    object Empty : FavouritesUiState()
    data class Success(val favourites: List<FavouriteEntity>) : FavouritesUiState()
    data class Error(val message: String) : FavouritesUiState()
}