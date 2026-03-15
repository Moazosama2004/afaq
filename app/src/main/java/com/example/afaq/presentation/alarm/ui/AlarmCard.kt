package com.example.afaq.presentation.alarm.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.afaq.R
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.presentation.favourites.ui.DeleteFavouriteDialog
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.formatAlertTime


// ─── Alert Card ───────────────────────────────────────────

@Composable
fun AlertCard(
    alert: AlertEntity,
    onDeleteClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }


    if (showDeleteDialog) {
        DeleteFavouriteDialog(
            cityName = alert.startTime.toString(),
            onConfirm = onDeleteClick,
            onDismiss = { showDeleteDialog = false }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AfaqThemeColors.secondry),
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
                    text = "${stringResource(R.string.start)}: ${formatAlertTime(alert.startTime)}",
                    style = AfaqTypography.semiBold14,
                    color = AfaqThemeColors.textPrimary
                )
                Text(
                    text = "${stringResource(R.string.end)}: ${formatAlertTime(alert.endTime)}",
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

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}
