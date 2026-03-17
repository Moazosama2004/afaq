package com.example.afaq.presentation.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.afaq.data.home.model.Weather
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.converters.TempConverter
import com.example.afaq.utils.formatDate
import com.example.afaq.utils.formatLastUpdated
import com.example.afaq.utils.getWeatherIcon


@Composable
fun WeatherCard(
    weather: Weather,
    tempUnit: String,
    currentTime: Long,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AfaqThemeColors.secondry
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = AfaqThemeColors.background,
                            shape = RoundedCornerShape(30.dp),
                        )
                        .padding(10.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = AfaqThemeColors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatDate(weather.date.toLong()),
                        style = AfaqTypography.regular12,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = AfaqThemeColors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${weather.cityName} , ${weather.country}",
                            style = AfaqTypography.bold16,
                            color = AfaqThemeColors.primary,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = AfaqThemeColors.background,
                            shape = RoundedCornerShape(30.dp),
                        )
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = AfaqThemeColors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(
                    getWeatherIcon(weather.temperature, weather.icon)
                ),
                contentDescription = weather.description,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = TempConverter.convert(weather.temperature.toDouble(), tempUnit),
                    style = AfaqTypography.bold32,
                    color = AfaqThemeColors.primary,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = TempConverter.convert(weather.feelsLike.toDouble(), tempUnit),
                    style = AfaqTypography.regular16,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatLastUpdated(context, weather.lastUpdated),
                style = AfaqTypography.regular12,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weather.description,
                style = AfaqTypography.bold20,
                color = AfaqThemeColors.primary,
            )
        }
    }
}
