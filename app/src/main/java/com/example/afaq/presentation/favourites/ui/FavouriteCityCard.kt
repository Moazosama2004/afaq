package com.example.afaq.presentation.favourites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.afaq.data.local.db.FavouriteEntity
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.localizeDigits

@Composable
fun FavouriteCityCard(
    favourite: FavouriteEntity,
    onCardClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }


    if (showDeleteDialog) {
        DeleteFavouriteDialog(
            cityName = favourite.cityName,
            onConfirm = onDeleteClick,
            onDismiss = { showDeleteDialog = false }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = AfaqThemeColors.secondry
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AfaqThemeColors.primary.copy(alpha = 0.08f),
                            AfaqThemeColors.secondry.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left - Location Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Location Icon Box
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AfaqThemeColors.primary,
                                        AfaqThemeColors.secondry
                                    )
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${favourite.icon}@2x.png",
                            contentDescription = favourite.description,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = favourite.cityName,
                            style = AfaqTypography.semiBold16,
                            color = AfaqThemeColors.textPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AfaqThemeColors.textSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = favourite.country,
                                style = AfaqTypography.regular12,
                                color = AfaqThemeColors.textSecondary
                            )
                        }
                        Text(
                            text = favourite.description.replaceFirstChar { it.uppercase() },
                            style = AfaqTypography.regular12,
                            color = AfaqThemeColors.primary
                        )
                    }
                }

                // Right - Temperature + Delete
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Temperature
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = favourite.temperature.toInt().toString().localizeDigits(),
                            style = AfaqTypography.bold32,
                            color = AfaqThemeColors.textPrimary
                        )
                        Text(
                            text = "C",
                            style = AfaqTypography.regular12,
                            color = AfaqThemeColors.textSecondary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    // Min / Max
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = AfaqThemeColors.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = favourite.tempMax.toInt().toString().localizeDigits(),
                            style = AfaqTypography.regular12,
                            color = AfaqThemeColors.textPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = AfaqThemeColors.secondry,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = favourite.tempMin.toInt().toString().localizeDigits(),
                            style = AfaqTypography.regular12,
                            color = AfaqThemeColors.textPrimary
                        )
                    }

                    // Delete button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(0xFFFFEEEE),
                                shape = CircleShape
                            )
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = androidx.compose.ui.graphics.Color(0xFFE53935),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Humidity + Wind badges
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 78.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeatherBadge(
                    icon = Icons.Default.WaterDrop,
                    value = "${favourite.humidity}%".localizeDigits(),
                    color = AfaqThemeColors.secondry
                )
                WeatherBadge(
                    icon = Icons.Default.Air,
                    value = "${favourite.windSpeed.toInt()} m/s".localizeDigits(),
                    color = AfaqThemeColors.primary
                )
            }
        }
    }
}