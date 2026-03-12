package com.example.afaq

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.afaq.data.settings.SettingsKeys
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.data.settings.dataStore
import com.example.afaq.presentation.BottomNavBar
import com.example.afaq.presentation.navigation.AppNavigation
import com.example.afaq.presentation.settings.manager.SettingsViewModel
import com.example.afaq.presentation.settings.manager.SettingsViewModelFactory
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTheme
import com.example.afaq.utils.LocaleHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingsRepo(this))
    }

    override fun attachBaseContext(base: Context) {
        val lang = runBlocking {
            base.dataStore.data
                .map { it[SettingsKeys.LANGUAGE] ?: "English" }
                .first()
        }
        super.attachBaseContext(LocaleHelper.setLocale(base, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AfaqTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // hide bottom bar on splash screen
                val showBottomBar = currentRoute?.contains("SplashRoute") == false

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    },
                    containerColor = AfaqThemeColors.background,

                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        settingsViewModel = settingsViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


