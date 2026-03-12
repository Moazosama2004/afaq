package com.example.afaq.presentation.screens.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.afaq.data.location.LocationRepository
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.presentation.splash.screens.SplashContent
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(onLocationReady: (lat: Double, lon: Double) -> Unit) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationRepository = remember { LocationRepository(context) }
    val settingsRepo = remember { SettingsRepo(context) }

    var showRationale by remember { mutableStateOf(false) }
    var showGpsDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }

    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    // helper to save + navigate
    fun saveAndNavigate(lat: Double, lon: Double) {
        scope.launch {
            settingsRepo.saveUserLocation(lat, lon)
        }
        onLocationReady(lat, lon)
    }

    // Step 1 - check GPS
    LaunchedEffect(Unit) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            showGpsDialog = true
            return@LaunchedEffect
        }
    }

    // Step 2 - handle permission
    LaunchedEffect(locationPermission.status, permissionRequested) {
        if (showGpsDialog) return@LaunchedEffect

        when {
            locationPermission.status.isGranted -> {
                withTimeoutOrNull(10000L) {
                    locationRepository.getUserLocation().collect { (lat, lon) ->
                        saveAndNavigate(lat, lon)
                    }
                } ?: saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
            }

            locationPermission.status.shouldShowRationale -> {
                showRationale = true
                return@LaunchedEffect
            }

            else -> {
                if (!permissionRequested) {
                    permissionRequested = true
                    locationPermission.launchPermissionRequest()
                } else {
                    showSettingsDialog = true
                }
            }
        }
    }

    // ─── GPS Dialog ───────────────────────────────────────
    if (showGpsDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    text = "GPS is Off",
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "Please enable GPS for accurate weather data.",
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showGpsDialog = false
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                }) {
                    Text("Enable GPS", color = AfaqThemeColors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showGpsDialog = false
                    saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                }) {
                    Text("Use Default", color = AfaqThemeColors.textSecondary)
                }
            }
        )
    }

    // ─── Settings Dialog ──────────────────────────────────
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    text = "Permission Required",
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "Location permission was denied. Please enable it from Settings for accurate weather.",
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                    saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                }) {
                    Text("Go to Settings", color = AfaqThemeColors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                }) {
                    Text("Use Default", color = AfaqThemeColors.textSecondary)
                }
            }
        )
    }

    // ─── Rationale Dialog ─────────────────────────────────
    if (showRationale) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    text = "Location Required",
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "Afaq needs your location to show accurate weather.",
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    locationPermission.launchPermissionRequest()
                }) {
                    Text("Allow", color = AfaqThemeColors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationale = false
                    saveAndNavigate(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                }) {
                    Text("Skip", color = AfaqThemeColors.textSecondary)
                }
            }
        )
    }

    SplashContent()
}
