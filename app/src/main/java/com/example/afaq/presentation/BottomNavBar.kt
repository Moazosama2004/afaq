package com.example.afaq.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.presentation.navigation.Routes
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.Constants

@Composable
fun BottomNavBar(navController: NavHostController) {

    val context = LocalContext.current
    val settingsRepo = remember { SettingsRepo(context) }
    val lat by settingsRepo.userLat.collectAsState(initial = Constants.DEFAULT_LAT)
    val lon by settingsRepo.userLon.collectAsState(initial = Constants.DEFAULT_LON)

    val navItems = rememberBottomNavItems(lat = lat, lon = lon)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = AfaqThemeColors.background,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute?.contains(
                item.route::class.simpleName ?: ""
            ) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = item.label, style = AfaqTypography.regular12)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AfaqThemeColors.primary,
                    selectedTextColor = AfaqThemeColors.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = AfaqThemeColors.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}