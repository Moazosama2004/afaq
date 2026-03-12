package com.example.afaq.presentation.home.ui

import android.icu.util.TimeUnit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.afaq.R
import com.example.afaq.data.home.model.Weather
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.TempConverter

@Composable
fun DayDetailsGrid(
    weather: Weather,
    tempUnit: String,
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = AfaqThemeColors.secondry)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailItem(
                value = TempConverter.convert(weather.feelsLike.toDouble(), tempUnit),
                unit = "",
                label = stringResource(R.string.feels_like)
            )
            DetailItem(
                value = "${weather.humidity}%",
                unit = "",
                label = stringResource(R.string.humidity)
            )
        }

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailItem(
                value = "${weather.visibility / 1000}",
                unit = "KM",
                unitSize = 10.sp,
                label = stringResource(R.string.visibility)
            )
            DetailItem(
                value = "${weather.windSpeed.toInt()}",
                unit = "km/h",
                unitSize = 10.sp,
                label = stringResource(R.string.wind)
            )
        }

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailItem(
                value = TempConverter.convert(weather.tempMin.toDouble(), tempUnit),
                unit = "",
                label = stringResource(R.string.dew_point)
            )

            // Min/Max row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Max",
                    tint = Color(0xFF31507F),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = TempConverter.convert(weather.tempMax.toDouble(), tempUnit),
                    style = AfaqTypography.bold16,
                    color = Color(0xFF31507F)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Min",
                    tint = Color(0xFF31507F),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = TempConverter.convert(weather.tempMin.toDouble(), tempUnit),
                    style = AfaqTypography.bold16,
                    color = Color(0xFF31507F)
                )
            }
        }
    }
}

