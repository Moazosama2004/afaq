package com.example.afaq.presentation.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AfaqColors.darkPrimary,
    secondary = AfaqColors.darkSecondary,
    background = AfaqColors.darkBackground,
    surface = AfaqColors.darkSurface,
    onBackground = AfaqColors.darkTextPrimary,
    onSurface = AfaqColors.darkTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AfaqColors.primary,
    secondary = AfaqColors.secondary,
    background = AfaqColors.background,
    surface = AfaqColors.surface,
    onBackground = AfaqColors.textPrimary,
    onSurface = AfaqColors.textPrimary
)

@Composable
fun AfaqTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}