package com.example.afaq.presentation.favourites.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.afaq.R
import com.example.afaq.presentation.theme.theme.AfaqColors
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography

@Composable
fun OfflineDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AfaqThemeColors.secondry,
        title = {
            Text(
                text = stringResource(R.string.no_internet),
                style = AfaqTypography.bold16,
                color = AfaqThemeColors.textPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.offline_city_view_error),
                style = AfaqTypography.regular14,
                color = AfaqThemeColors.textSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.ok), color = AfaqThemeColors.primary)
            }
        },
        icon = {
            Icon(
                Icons.Default.WifiOff,
                contentDescription = null,
                tint = AfaqColors.warning,
                modifier = Modifier.size(32.dp)
            )
        }
    )
}