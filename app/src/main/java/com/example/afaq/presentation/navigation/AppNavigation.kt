package com.example.afaq.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.afaq.presentation.alerts.ui.AlertsScreen
import com.example.afaq.presentation.favourites.ui.FavouritesScreen
import com.example.afaq.presentation.home.ui.HomeScreen
import com.example.afaq.presentation.screens.splash.SplashScreen
import com.example.afaq.presentation.settings.manager.SettingsViewModel
import com.example.afaq.presentation.settings.ui.SettingsScreen

@Composable
fun AppNavigation(
    settingsViewModel: SettingsViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SplashRoute,
        modifier = modifier
    ) {
        composable<Routes.SplashRoute> {
            SplashScreen(
                onLocationReady = {
                    navController.navigate(Routes.HomeRoute) {
                        popUpTo(Routes.SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.HomeRoute> {
            HomeScreen(                              // ← no lat/lon from route ✅
                settingsViewModel = settingsViewModel
            )
        }

        composable<Routes.FavouritesRoute> {
            FavouritesScreen()
        }

        composable<Routes.AlertsRoute> {
            AlertsScreen()
        }

        composable<Routes.SettingsRoute> {
            SettingsScreen(
                settingsViewModel = settingsViewModel
            )
        }
    }
}