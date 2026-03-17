package com.example.afaq.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.afaq.presentation.connectivity.NetworkViewModel
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.data.network.NetworkStatus

@Composable
fun BottomNavBar(
    navController: NavHostController,
    networkViewModel: NetworkViewModel
) {

    val navItems = rememberBottomNavItems()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val networkStatus by networkViewModel.networkStatus.collectAsState()

    NavigationBar(
        containerColor = if (networkStatus == NetworkStatus.Offline)
            AfaqThemeColors.background.copy(alpha = 0.7f)
        else
            AfaqThemeColors.background,
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