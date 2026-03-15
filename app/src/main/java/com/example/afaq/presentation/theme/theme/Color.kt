package com.example.afaq.presentation.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AfaqColors {

    // ─── Light Colors ─────────────────────────────────────
    val primary = Color(0xFF31507F)
    val secondary = Color(0xFFFFFFFF)
    val background = Color(0xFFF4F4F4)
    val surface = Color(0xFF31507F)
    val textPrimary = Color(0xFF1A1A2E)
    val textSecondary = Color(0xFF6B7280)
    val textWhite = Color(0xFFFFFFFF)
    val textBlack = Color(0xFF000000)
    val dialog = Color(0xFFFFFFFF)

    // ─── Dark Colors ──────────────────────────────────────
    val darkPrimary = Color(0xFFFFFFFF)
    val darkSecondary = Color(0xFF000000)
    val darkBackground = Color(0xFF1F1F1F)
    val darkSurface = Color(0xFF31507F)
    val darkTextPrimary = Color(0xFFFFFFFF)
    val darkTextSecondary = Color(0xFFFFFFFF)
    val darkDialog = Color(0xFF1F1F1F)
    val darkTextBlack = Color(0xFFFFFFFF)

    // ─── Status ───────────────────────────────────────────
    val success = Color(0xFF4CAF50)
    val error = Color(0xFFE53935)
    val warning = Color(0xFFFFB300)
}

// ─── Theme-aware colors ───────────────────────────────────
// Use these in your screens instead of AfaqColors.xxx directly


object AfaqThemeColors {
    val primary: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkPrimary else AfaqColors.primary

    val secondry: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkSecondary else AfaqColors.secondary

    val background: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkBackground else AfaqColors.background

    val surface: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkSurface else AfaqColors.surface

    val textPrimary: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkTextPrimary else AfaqColors.textPrimary

    val textSecondary: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkTextSecondary else AfaqColors.textSecondary

    val dialog: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkDialog else AfaqColors.dialog

    val textBlack: Color
        @Composable get() = if (isSystemInDarkTheme()) AfaqColors.darkTextBlack else AfaqColors.textBlack
}