package com.example.afaq.presentation.alarm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.afaq.R
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.presentation.theme.theme.AfaqColors
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.services.alarms.AndroidAlarmManager
import com.example.afaq.services.workmanager.WorkManagerScheduler
import com.example.afaq.utils.formatAlertTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    onDismiss: () -> Unit,
    onSave: (startTime: Long, endTime: Long, type: String) -> Unit
) {
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("ALARM") }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    var startTimeMs by remember { mutableStateOf(0L) }
    var endTimeMs by remember { mutableStateOf(0L) }
    var startError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AfaqThemeColors.secondry,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = stringResource(R.string.start_duration),
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.textSecondary
            )
            OutlinedCard(
                onClick = { showStartPicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = AfaqThemeColors.secondry,
                    contentColor = AfaqThemeColors.textPrimary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (startError) AfaqColors.error else AfaqThemeColors.primary
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AfaqThemeColors.primary
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.start_duration),
                            style = AfaqTypography.regular12,
                            color = AfaqThemeColors.primary
                        )
                        Text(
                            text = if (startTime.isEmpty()) stringResource(R.string.select_start_time) else startTime,
                            style = AfaqTypography.semiBold14,
                            color = AfaqThemeColors.textPrimary
                        )
                    }
                }
            }
            if (startError) {
                Text(
                    text = stringResource(R.string.start_time_must_be_in_the_future),
                    style = AfaqTypography.regular12,
                    color = Color.Red
                )
            }

            Text(
                text = stringResource(R.string.end_duration),
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.textSecondary
            )
            OutlinedCard(
                onClick = { showEndPicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = AfaqThemeColors.secondry,
                    contentColor = AfaqThemeColors.textPrimary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = AfaqThemeColors.primary
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AfaqThemeColors.textSecondary
                    )
                    Text(
                        text = if (endTime.isEmpty()) stringResource(R.string.end_duration) else endTime,
                        style = AfaqTypography.semiBold14,
                        color = AfaqThemeColors.textPrimary
                    )
                }
            }

            Text(
                text = stringResource(R.string.notify_me_by),
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.textSecondary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                listOf(
                    stringResource(R.string.alarm),
                    stringResource(R.string.notification)
                ).forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AfaqThemeColors.primary
                            )
                        )
                        Text(
                            text = type.lowercase().replaceFirstChar { it.uppercase() },
                            style = AfaqTypography.regular14,
                            color = AfaqThemeColors.textPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        startError = startTimeMs < System.currentTimeMillis()
                        if (!startError && endTimeMs > startTimeMs) {
                            onSave(startTimeMs, endTimeMs, selectedType)
                            var alertEntity = AlertEntity(
                                startTime = startTimeMs,
                                endTime = endTimeMs,
                                type = selectedType
                            )
                            when (alertEntity.type) {
                                "ALARM" -> {
                                    val alarmManager = AndroidAlarmManager(context)
                                    alarmManager.schedule(alertEntity)
                                }

                                "NOTIFICATION" -> {
                                    WorkManagerScheduler(context).schedule(alertEntity)
                                }
                            }


                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stringResource(R.string.save),
                        color = Color.White,
                        style = AfaqTypography.semiBold14
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = Color.White,
                        style = AfaqTypography.semiBold14
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showStartPicker) {
        TimePickerDialog(
            onDismiss = { showStartPicker = false },
            onConfirm = { hour, minute ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                startTimeMs = cal.timeInMillis
                startTime = formatAlertTime(startTimeMs)
                startError = startTimeMs < System.currentTimeMillis()
                showStartPicker = false
            }
        )
    }

    if (showEndPicker) {
        TimePickerDialog(
            onDismiss = { showEndPicker = false },
            onConfirm = { hour, minute ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                endTimeMs = cal.timeInMillis
                endTime = formatAlertTime(endTimeMs)
                showEndPicker = false
            }
        )
    }
}
