package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.afaq.data.model.ForecastItem
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.formatForecastDate
import com.example.afaq.utils.getWeatherIcon
import com.example.afaq.utils.localizeDigits

@Composable
fun ForecastDayCard(item: ForecastItem) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AfaqThemeColors.secondry
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.description,
                style = AfaqTypography.semiBold14,
                color = AfaqThemeColors.primary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formatForecastDate(item.dt),
                style = AfaqTypography.regular12,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(
                    id = getWeatherIcon(item.temp, item.icon)
                ),
                contentDescription = item.description,
                modifier = Modifier.size(48.dp)
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.temp.toInt().toString().localizeDigits(),
                    style = AfaqTypography.bold20,
                    color = Color(0xFF31507F)
                )
                Text(
                    text = item.tempMin.toInt().toString().localizeDigits(),
                    style = AfaqTypography.regular12,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "C",
                    style = AfaqTypography.regular12,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
        }
    }
}
