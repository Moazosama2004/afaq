package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.afaq.data.model.Forecast
import com.example.afaq.utils.isSameDay

@Composable
fun HourlyForecastRow(
    forecast: Forecast,
    tempUnit : String,
    ) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // filter today's items only (every 3 hours)
        items(
            forecast.items
                .filter { isSameDay(it.dt) }
                .take(8) // max 8 entries = 24 hours
        ) { item ->
            HourlyCard(item = item,tempUnit = tempUnit)
        }
    }
}
