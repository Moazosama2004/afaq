package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.afaq.data.model.Forecast

@Composable
fun ForecastRow(forecast: Forecast) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(forecast.items.filter { it.dtTxt.contains("12:00:00") }) { item ->
            ForecastDayCard(item = item)
        }
    }
}


