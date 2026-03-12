package com.example.afaq.presentation.alarms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.utils.formatAlertTime
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography


// ─── Alert Card ───────────────────────────────────────────

@Composable
fun AlertCard(
    alert: AlertEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Start: ${formatAlertTime(alert.startTime)}",
                    style = AfaqTypography.semiBold14,
                    color = AfaqThemeColors.textPrimary
                )
                Text(
                    text = "End: ${formatAlertTime(alert.endTime)}",
                    style = AfaqTypography.regular12,
                    color = AfaqThemeColors.textSecondary
                )
                // Type badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (alert.type == "ALARM")
                        AfaqThemeColors.primary.copy(alpha = 0.1f)
                    else
                        AfaqThemeColors.secondry.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = alert.type,
                        style = AfaqTypography.regular12,
                        color = if (alert.type == "ALARM") AfaqThemeColors.primary else AfaqThemeColors.secondry,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}
