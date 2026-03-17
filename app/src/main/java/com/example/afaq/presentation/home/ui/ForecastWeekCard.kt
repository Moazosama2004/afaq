package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.afaq.R
import com.example.afaq.data.model.ForecastItem
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.converters.TempConverter
import com.example.afaq.utils.formatForecastDate
import com.example.afaq.utils.getDayName
import com.example.afaq.utils.getWeatherIcon

@Composable
fun ForecastWeekCard(
    item: ForecastItem,
    tempUnit: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AfaqThemeColors.secondry,
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = getDayName(item.dt),
                    style = AfaqTypography.semiBold16,
                    color = AfaqThemeColors.textPrimary
                )
                Text(
                    text = formatForecastDate(item.dt),
                    style = AfaqTypography.regular12,
                    color = AfaqThemeColors.textSecondary
                )
            }

            Image(
                painter = painterResource(
                    id = getWeatherIcon(item.temp, item.icon)
                ),
                contentDescription = item.description,
                modifier = Modifier.size(50.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = TempConverter.convert(item.temp, tempUnit),
                        style = AfaqTypography.bold20,
                        color = AfaqThemeColors.textPrimary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.feels_like),
                        style = AfaqTypography.regular12,
                        color = AfaqThemeColors.textSecondary
                    )
                    Text(
                        text = TempConverter.convert(item.feelsLike, tempUnit),
                        style = AfaqTypography.semiBold14,
                        color = AfaqThemeColors.primary
                    )
                }
            }
        }
    }
}
