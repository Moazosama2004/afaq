package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.afaq.R
import com.example.afaq.data.model.ForecastItem
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.TempConverter
import com.example.afaq.utils.formatHour

@Composable
fun HourlyCard(
    item: ForecastItem,
    tempUnit: String,
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AfaqThemeColors.secondry
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Time
            Text(
                text = formatHour(item.dt),
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.textPrimary,
                textAlign = TextAlign.Center
            )

            // Weather Icon
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${item.icon}@2x.png",
                contentDescription = item.description,
                modifier = Modifier.size(48.dp)
            )

            // Temperature
            Text(
                text = TempConverter.convert(item.temp, tempUnit),
                style = AfaqTypography.bold16,
                color = AfaqThemeColors.primary,
                textAlign = TextAlign.Center
            )

            // Divider
            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                thickness = 1.dp
            )

            // Feels like
            Text(
                text = stringResource(R.string.feels),
                style = AfaqTypography.regular12,
                color = AfaqThemeColors.textSecondary,
                textAlign = TextAlign.Center
            )
            Text(
                text = TempConverter.convert(item.feelsLike, tempUnit),
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}