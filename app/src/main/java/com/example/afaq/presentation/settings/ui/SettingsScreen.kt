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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.afaq.utils.fetchGpsLocation
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by settingsViewModel.language.collectAsState()
    val selectedTempUnit by settingsViewModel.tempUnit.collectAsState()
    val selectedLocation by settingsViewModel.location.collectAsState()
    val selectedWindUnit by settingsViewModel.windUnit.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showMapSheet by remember { mutableStateOf(false) }

    // ─── Map keys to display strings ──────────────────────
    val languageOptions = mapOf(
        "Arabic" to stringResource(R.string.arabic),
        "English" to stringResource(R.string.english),
        "Default" to stringResource(R.string.lang_default)
    )
    val tempOptions = mapOf(
        "Celsius °C" to stringResource(R.string.celsius_c),
        "Kelvin °K" to stringResource(R.string.kelvin_k),
        "Fahrenheit °F" to stringResource(R.string.fahrenheit_f)
    )
    val locationOptions = mapOf(
        "GPS" to stringResource(R.string.gps),
        "Map" to stringResource(R.string.map)
    )
    val windOptions = mapOf(
        "meter/sec" to stringResource(R.string.meter_sec),
        "mile/hour" to stringResource(R.string.mile_hour)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AfaqThemeColors.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(AfaqThemeColors.primary, AfaqThemeColors.secondry)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings),
                    style = AfaqTypography.bold32,
                    color = AfaqThemeColors.secondry
                )
                Text(
                    text = stringResource(R.string.customize_your_experience),
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.secondry.copy(alpha = 0.8f)
                )
            }
        }

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
                subtitle = languageOptions[selectedLanguage] ?: selectedLanguage // ← translated ✅
            ) {
                FancyRadioGroup(
                    options = languageOptions.values.toList(), // ← translated options ✅
                    selected = languageOptions[selectedLanguage] ?: selectedLanguage,
                    onSelect = { displayValue ->
                        // convert display back to key
                        val key = languageOptions.entries
                            .find { it.value == displayValue }?.key ?: displayValue
                        scope.launch {
                            settingsViewModel.setLanguage(key) // ← save key ✅
                            val intent = (context as Activity).intent
                            (context as Activity).finish()
                            context.startActivity(intent)
                        }
                    }
                )
            }

            // Temp Unit
            FancySettingsCard(
                icon = Icons.Default.Thermostat,
                iconGradient = listOf(Color(0xFFFF6B35), Color(0xFFFF9A3C)),
                title = stringResource(R.string.temp_unit),
                subtitle = tempOptions[selectedTempUnit] ?: selectedTempUnit // ← translated ✅
            ) {
                FancyRadioGroup(
                    options = tempOptions.values.toList(), // ← translated ✅
                    selected = tempOptions[selectedTempUnit] ?: selectedTempUnit,
                    onSelect = { displayValue ->
                        val key = tempOptions.entries
                            .find { it.value == displayValue }?.key ?: displayValue
                        settingsViewModel.setTempUnit(key) // ← save key ✅
                    }
                )
            }

            // Location
            FancySettingsCard(
                icon = Icons.Default.LocationOn,
                iconGradient = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)),
                title = stringResource(R.string.location),
                subtitle = locationOptions[selectedLocation] ?: selectedLocation // ← translated ✅
            ) {
                FancyRadioGroup(
                    options = locationOptions.values.toList(), // ← translated ✅
                    selected = locationOptions[selectedLocation] ?: selectedLocation,
                    onSelect = { displayValue ->
                        val key = locationOptions.entries
                            .find { it.value == displayValue }?.key ?: displayValue
                        settingsViewModel.setLocation(key)
                        if (key == "Map") {
                            showMapSheet = true
                        } else if (key == "GPS") {
                            scope.launch {
                                fetchGpsLocation(context, settingsViewModel)
                            }
                        }
                    }
                )
            }

            // Wind Speed
            FancySettingsCard(
                icon = Icons.Default.Air,
                iconGradient = listOf(Color(0xFFFFD60A), Color(0xFFFFB300)),
                title = stringResource(R.string.wind_speed_unit),
                subtitle = windOptions[selectedWindUnit] ?: selectedWindUnit // ← translated ✅
            ) {
                FancyRadioGroup(
                    options = windOptions.values.toList(), // ← translated ✅
                    selected = windOptions[selectedWindUnit] ?: selectedWindUnit,
                    onSelect = { displayValue ->
                        val key = windOptions.entries
                            .find { it.value == displayValue }?.key ?: displayValue
                        settingsViewModel.setWindUnit(key) // ← save key ✅
                    }
                )
            }
        }
    }

    if (showMapSheet) {
        LocationMapBottomSheet(
            onDismiss = { showMapSheet = false },
            onLocationSelected = { lat, lon ->
                scope.launch {
                    settingsViewModel.saveUserLocation(lat, lon)
                    showMapSheet = false
                }
            }
        )
    }
}

