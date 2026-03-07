package com.example.afaq.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.afaq.presentation.home.manager.HomeViewModel
import com.example.afaq.presentation.home.screens.HomeScreen
import com.example.afaq.presentation.splash.screens.SplashScreen

@Composable
fun AppNavigation(
    navController : NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Routes.SplashRoute
    ){
        composable<Routes.SplashRoute> {
            SplashScreen (
            ){
                navController.navigate(Routes.HomeRoute) {
                    popUpTo(Routes.SplashRoute) {
                        inclusive = true
                    }
                }
            }
        }

        composable<Routes.HomeRoute> {
            HomeScreen(
                viewModel = viewModel<HomeViewModel>(),
            )
        }
    }
}

// 1. create routes
// 2. nav controller
// 3. nav host