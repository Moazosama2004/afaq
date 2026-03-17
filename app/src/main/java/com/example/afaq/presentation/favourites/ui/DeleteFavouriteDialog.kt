package com.example.afaq.presentation.favourites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.afaq.R
import com.example.afaq.presentation.theme.theme.AfaqColors
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography

@Composable
fun DeleteFavouriteDialog(
    cityName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.delete_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AfaqThemeColors.dialog
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Lottie animation
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(120.dp)
                )

                // Title
                Text(
                    text = "Remove Favourite",
                    style = AfaqTypography.bold20,
                    color = AfaqThemeColors.textPrimary,
                    textAlign = TextAlign.Center
                )

                // Message
                Text(
                    text = "Are you sure you want to remove \"$cityName\" from your favourites?",
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, AfaqThemeColors.primary
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            color = AfaqThemeColors.primary,
                            style = AfaqTypography.semiBold14
                        )
                    }

                    // Delete
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AfaqColors.error
                        )
                    ) {
                        Text(
                            text = "Remove",
                            color = Color.White,
                            style = AfaqTypography.semiBold14
                        )
                    }
                }
            }
        }
    }
}