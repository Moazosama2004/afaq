package com.example.afaq.presentation.theme.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.afaq.R

val SpaceGroteskFamily = FontFamily(
    Font(R.font.space_grotesk_light, FontWeight.Light),
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_medium, FontWeight.Medium),
    Font(R.font.space_grotesk_semibold, FontWeight.SemiBold),
    Font(R.font.space_grotesk_bold, FontWeight.Bold)
)

object AfaqTypography {

    // Bold
    val bold32 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    )
    val bold24 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    val bold20 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
    val bold16 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
    val bold14 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    // SemiBold
    val semiBold32 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    )

    val semiBold28 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    )

    val semiBold24 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    )
    val semiBold20 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    )
    val semiBold16 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )
    val semiBold14 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )

    // Medium
    val medium16 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    val medium14 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
    val medium12 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

    // Regular
    val regular16 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    val regular14 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
    val regular12 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    // Light
    val light14 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
    val light12 = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
}