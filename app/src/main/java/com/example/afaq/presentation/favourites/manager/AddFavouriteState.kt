package com.example.afaq.presentation.favourites.manager

sealed class AddFavouriteState {
    object Idle : AddFavouriteState()
    object Loading : AddFavouriteState()
    object Success : AddFavouriteState()
    data class Error(val message: String) : AddFavouriteState()
}