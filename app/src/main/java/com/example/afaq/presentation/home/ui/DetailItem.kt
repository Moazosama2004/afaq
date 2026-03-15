package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography

@Composable
fun DetailItem(
    value: String,
    unit: String,
    label: String,
    unitSize: TextUnit = 14.sp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Value + Unit
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = AfaqTypography.bold16,
                color = AfaqThemeColors.primary,
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    fontSize = unitSize,
                    fontWeight = FontWeight.Normal,
                    color = AfaqThemeColors.primary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        // Label
        Text(
            text = label,
            style = AfaqTypography.regular14,
            color = AfaqThemeColors.primary,
        )
    }
}