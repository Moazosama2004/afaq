package com.example.afaq.presentation.alerts.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.afaq.data.alarm.AlertRepo
import com.example.afaq.data.alarm.datasource.local.AlertLocalDataSource
import com.example.afaq.db.AppDatabase
import com.example.afaq.presentation.alarms.manager.AlertViewModel
import com.example.afaq.presentation.alarms.manager.AlertViewModelFactory
import com.example.afaq.presentation.alarms.manager.AlertsUiState
import com.example.afaq.presentation.alarms.ui.AddAlertBottomSheet
import com.example.afaq.presentation.alarms.ui.AlertCard
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.services.alarms.AndroidAlarmManager
import com.example.afaq.services.workmanager.WorkManagerScheduler
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val viewModel = viewModel<AlertViewModel>(
        factory = remember {
            AlertViewModelFactory(
                AlertRepo(
                    AlertLocalDataSource(AppDatabase.getInstance(context).alertDao())
                )
            )
        }
    )

    val alertsState by viewModel.alertsState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }

    // Notification permission (Android 13+)

    var hasPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            mutableStateOf(
                true
            )
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // ✅ User allowed
                hasPermission = true

            } else {
                // ❌ User disallowed — check if permanently denied
                val activity = context as Activity

                val permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )

                if (permanentlyDenied) {
                    // User clicked "Don't ask again" → send to Settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                } else {
                    // User just clicked Disallow → show rationale
                    showRationale = true
                }
            }
        }
    )



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AfaqThemeColors.background)
    ) {
        when (alertsState) {
            is AlertsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AfaqThemeColors.primary
                )
            }

            is AlertsUiState.Empty -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AfaqThemeColors.primary,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = "You haven't any alarms",
                        style = AfaqTypography.semiBold16,
                        color = AfaqThemeColors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is AlertsUiState.Success -> {
                val alerts = (alertsState as AlertsUiState.Success).alerts
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(alerts) { alert ->
                        AlertCard(
                            alert = alert,
                            onDeleteClick = {
                                viewModel.deleteAlert(alert.id)
                                WorkManagerScheduler(context).cancel(alert.id)
                                AndroidAlarmManager(context).cancel(alert)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            is AlertsUiState.Error -> {
                Text(
                    text = (alertsState as AlertsUiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // FAB
        FloatingActionButton(
            onClick = {
                // check permission first
                if(!hasPermission) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }else {
                    showBottomSheet = true
                }

            },
            containerColor = AfaqThemeColors.primary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Alert")
        }
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Permission Needed") },
            text = { Text("Notifications are needed to alert you on time.") },
            confirmButton = {
                Button(onClick = {
                    showRationale = false
                    // ask again
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) { Text("Try Again") }
            },
            dismissButton = {
                TextButton(onClick = { showRationale = false }) { Text("Cancel") }
            }
        )
    }

    // Bottom Sheet
    if (showBottomSheet) {
        AddAlertBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSave = { startTime, endTime, type ->
                Log.d("AddAlertBottomSheet" , "$startTime, $endTime, $type")
                viewModel.addAlert(startTime, endTime, type)
                showBottomSheet = false
            }
        )
    }
}




