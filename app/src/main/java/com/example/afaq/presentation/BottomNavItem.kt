package com.example.afaq.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.afaq.R
import com.example.afaq.presentation.navigation.Routes
import com.example.afaq.utils.getAppLocale

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun rememberBottomNavItems(lat: Double, lon: Double): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            label = stringResource(R.string.home),
            icon = Icons.Default.Home,
            route = Routes.HomeRoute(lat, lon)
        ),
        BottomNavItem(
            label = stringResource(R.string.favourites),
            icon = Icons.Default.Favorite,
            route = Routes.FavouritesRoute
        ),
        BottomNavItem(
            label = stringResource(R.string.alerts),
            icon = Icons.Default.Notifications,
            route = Routes.AlertsRoute
        ),
        BottomNavItem(
            label = stringResource(R.string.settings),
            icon = Icons.Default.Settings,
            route = Routes.SettingsRoute
        )
    )
}