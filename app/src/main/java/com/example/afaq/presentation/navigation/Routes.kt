package com.example.afaq.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object SplashRoute : Routes()

    @Serializable
    data class HomeRoute(
        val lat: Double = 30.0444,
        val lon: Double = 31.2357
    ) : Routes()

    @Serializable
    object FavouritesRoute : Routes()

    @Serializable
    object AlertsRoute : Routes()

    @Serializable
    object SettingsRoute : Routes()
}