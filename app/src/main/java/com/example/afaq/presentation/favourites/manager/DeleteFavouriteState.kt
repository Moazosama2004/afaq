package com.example.afaq.presentation.favourites.manager

sealed class DeleteFavouriteState {
    object Idle : DeleteFavouriteState()
    object Loading : DeleteFavouriteState()
    object Success : DeleteFavouriteState()
    data class Error(val message: String) : DeleteFavouriteState()
}