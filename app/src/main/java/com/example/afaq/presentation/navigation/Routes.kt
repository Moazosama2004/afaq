package com.example.afaq.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object SplashRoute : Routes()

    @Serializable
    object HomeRoute : Routes()

    @Serializable
    object FavouritesRoute : Routes()

    @Serializable
    object AlertsRoute : Routes()

    @Serializable
    object SettingsRoute : Routes()
}