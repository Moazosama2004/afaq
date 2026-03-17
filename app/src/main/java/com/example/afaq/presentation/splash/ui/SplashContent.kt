package com.example.afaq.presentation.splash.ui

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.afaq.R
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import kotlinx.coroutines.delay

@Composable
fun SplashContent(onAnimationFinished: () -> Unit) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_bg)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false
    )

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.alert_sound)
    }


    LaunchedEffect(composition) {
        if (composition != null) {
            delay(1750L)
            mediaPlayer.start()
        }
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            onAnimationFinished()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AfaqThemeColors.primary),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
                .scale(4f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit,
            maintainOriginalImageBounds = true
        )

        // ← Text on top of Lottie
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Afaq",
                style = AfaqTypography.bold32,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}