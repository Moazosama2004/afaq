import com.example.afaq.data.local.db.FavouriteEntity

sealed class FavouritesUiState {
    object Loading : FavouritesUiState()
    object Empty : FavouritesUiState()
    data class Success(val favourites: List<FavouriteEntity>) : FavouritesUiState()
    data class Error(val message: String) : FavouritesUiState()
}

sealed class DeleteFavouriteState {
    object Idle : DeleteFavouriteState()
    object Loading : DeleteFavouriteState()
    object Success : DeleteFavouriteState()
    data class Error(val message: String) : DeleteFavouriteState()
}

sealed class AddFavouriteState {
    object Idle : AddFavouriteState()
    object Loading : AddFavouriteState()
    object Success : AddFavouriteState()
    data class Error(val message: String) : AddFavouriteState()
}