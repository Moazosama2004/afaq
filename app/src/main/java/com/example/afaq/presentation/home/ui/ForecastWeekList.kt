package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.afaq.data.model.Forecast

@Composable
fun ForecastWeekList(
    forecast: Forecast,
    tempUnit : String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        forecast.items
            .filter { it.dtTxt.contains("12:00:00") }
            .take(7) // ← max 7 days
            .forEach { item ->
                ForecastWeekCard(item = item,tempUnit = tempUnit)
            }
    }
}