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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.afaq.R
import com.example.afaq.data.location.LocationRepository
import com.example.afaq.data.settings.SettingsRepo
import com.example.afaq.presentation.splash.ui.SplashContent
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(onLocationReady: () -> Unit) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationRepository = remember { LocationRepository(context) }
    val settingsRepo = remember { SettingsRepo(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    var showRationale by remember { mutableStateOf(false) }
    var showGpsDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var hasAutoRequested by remember { mutableStateOf(false) }

    var animationFinished by remember { mutableStateOf(false) }
    var locationFinished by remember { mutableStateOf(false) }

    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(animationFinished, locationFinished) {
        if (animationFinished && locationFinished) {
            onLocationReady()
        }
    }

    suspend fun saveIfGps(lat: Double, lon: Double) {
        val locationPref = settingsRepo.location.first()
        if (locationPref == "GPS") {
            settingsRepo.saveUserLocation(lat, lon)
        }
    }

    fun locationDone() {
        locationFinished = true
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    if (locationPermission.status.isGranted) return@launch

                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                    if (!isGpsEnabled) {
                        showGpsDialog = true
                    } else {
                        when {
                            locationPermission.status.isGranted -> {}
                            locationPermission.status.shouldShowRationale -> {
                                showRationale = true
                            }

                            !hasAutoRequested -> {
                                hasAutoRequested = true
                                locationPermission.launchPermissionRequest()
                            }

                            else -> {
                                showSettingsDialog = true
                            }
                        }
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            val savedLat = settingsRepo.userLat.first()
            withTimeoutOrNull(5000L) {
                locationRepository.getUserLocation().collect { (lat, lon) ->
                    saveIfGps(lat, lon)
                    locationDone()
                }
            } ?: run {
                if (savedLat != null) {
                    locationDone()
                } else {
                    settingsRepo.saveUserLocation(Constants.DEFAULT_LAT, Constants.DEFAULT_LON)
                    locationDone()
                }
            }
        }
    }

    // ─── Dialogs ──────────────────────────────────────────
    if (showGpsDialog) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    stringResource(R.string.gps_off_title),
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    stringResource(R.string.gps_off_message),
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showGpsDialog = false
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }) {
                    Text(stringResource(R.string.enable_gps), color = AfaqThemeColors.primary)
                }
            }
        )
    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    stringResource(R.string.permission_required_title),
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    stringResource(R.string.permission_required_message),
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
                }) {
                    Text(stringResource(R.string.go_to_settings), color = AfaqThemeColors.primary)
                }
            }
        )
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = AfaqThemeColors.background,
            title = {
                Text(
                    stringResource(R.string.location_required_title),
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    stringResource(R.string.location_required_message),
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    locationPermission.launchPermissionRequest()
                }) {
                    Text(stringResource(R.string.allow), color = AfaqThemeColors.primary)
                }
            }
        )
    }

    SplashContent(
        onAnimationFinished = {
            animationFinished = true
        }
    )
}