package com.example.afaq.presentation.settings.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.afaq.R
import com.example.afaq.presentation.settings.manager.SettingsViewModel
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember { mutableStateOf(settingsViewModel.language.value) }
    var selectedTempUnit by remember { mutableStateOf(settingsViewModel.tempUnit.value) }
    var selectedLocation by remember { mutableStateOf("GPS") }
    var selectedWindUnit by remember { mutableStateOf("meter/sec") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AfaqThemeColors.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AfaqThemeColors.primary,
                            AfaqThemeColors.secondry
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings),
                    style = AfaqTypography.bold32,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.customize_your_experience),
                    style = AfaqTypography.regular14,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Settings Cards
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Language
            FancySettingsCard(
                icon = Icons.Default.Language,
                iconGradient = listOf(Color(0xFF4CC9F0), Color(0xFF0096C7)),
                title = stringResource(R.string.language),
                subtitle = selectedLanguage
            ) {
                FancyRadioGroup(
                    options = listOf("Arabic", "English", "Default"),
                    selected = selectedLanguage,
                    onSelect = { selectedLanguage = it
                        settingsViewModel.setLanguage(it)
                        // restart activity to apply new locale
                        val intent = (context as Activity).intent
                        context.finish()
                        context.startActivity(intent)
                    }
                )
            }

            // Temp Unit
            FancySettingsCard(
                icon = Icons.Default.Thermostat,
                iconGradient = listOf(Color(0xFFFF6B35), Color(0xFFFF9A3C)),
                title = stringResource(R.string.temp_unit),
                subtitle = selectedTempUnit
            ) {
                FancyRadioGroup(
                    options = listOf("Celsius °C", "Kelvin °K", "Fahrenheit °F"),
                    selected = selectedTempUnit,
                    onSelect = {
                        selectedTempUnit = it
                        settingsViewModel.setTempUnit(it)
                    }
                )
            }

            // Location
            FancySettingsCard(
                icon = Icons.Default.LocationOn,
                iconGradient = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)),
                title = stringResource(R.string.location),
                subtitle = selectedLocation
            ) {
                FancyRadioGroup(
                    options = listOf("GPS", "Map"),
                    selected = selectedLocation,
                    onSelect = { selectedLocation = it }
                )
            }

            // Wind Speed
            FancySettingsCard(
                icon = Icons.Default.Air,
                iconGradient = listOf(Color(0xFFFFD60A), Color(0xFFFFB300)),
                title = stringResource(R.string.wind_speed_unit),
                subtitle = selectedWindUnit
            ) {
                FancyRadioGroup(
                    options = listOf("meter/sec", "mile/hour"),
                    selected = selectedWindUnit,
                    onSelect = { selectedWindUnit = it }
                )
            }
        }
    }
}

