package com.example.afaq.presentation.favourites.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.afaq.R
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.presentation.theme.theme.LocalIsDarkTheme
import com.example.afaq.utils.getAddressFromLocation
import com.example.afaq.utils.getAppLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onLocationSelected: (lat: Double, lon: Double) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val isDarkTheme = LocalIsDarkTheme.current

    // init OSMDroid config
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    Box(modifier = modifier.fillMaxSize()) {

        // Map
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(7.0)
                    controller.setCenter(GeoPoint(26.8206, 30.8025)) // Egypt center

                    // Add current location overlay
                    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), this)
                    locationOverlay.enableMyLocation()
                    locationOverlay.runOnFirstFix {
                        post {
                            controller.animateTo(locationOverlay.myLocation)
                            controller.setZoom(15.0)
                        }
                    }
                    overlays.add(locationOverlay)

                    val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                            selectedPoint = p

                            overlays.removeAll { it is Marker }

                            val marker = Marker(this@apply).apply {
                                position = p
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = "${p.latitude}, ${p.longitude}"
                            }
                            overlays.add(marker)
                            invalidate()
                            return true
                        }

                        override fun longPressHelper(p: GeoPoint): Boolean = false
                    })
                    overlays.add(eventsOverlay)
                    mapView = this
                }
            },
            update = { view ->
                if (isDarkTheme) {
                    val matrix = ColorMatrix(floatArrayOf(
                        -1.0f, 0.0f, 0.0f, 0.0f, 255f,
                        0.0f, -1.0f, 0.0f, 0.0f, 255f,
                        0.0f, 0.0f, -1.0f, 0.0f, 255f,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                    ))
                    view.overlayManager.tilesOverlay.setColorFilter(ColorMatrixColorFilter(matrix))
                } else {
                    view.overlayManager.tilesOverlay.setColorFilter(null)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Back button
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(42.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape
                )
                .background(
                    color = if (isDarkTheme) AfaqThemeColors.background else Color.White,
                    shape = CircleShape
                )
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = if (isDarkTheme) Color.White else AfaqThemeColors.surface,
                modifier = Modifier.size(20.dp)
            )
        }

        // Selected location info + confirm button
        selectedPoint?.let { point ->

            var locationName by remember(point) {
                mutableStateOf(if (getAppLocale() == Locale("en")) "Loading..." else "الانتظار...")
            }

            LaunchedEffect(point) {
                locationName = withContext(Dispatchers.IO) {
                    getAddressFromLocation(context, point.latitude, point.longitude)
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AfaqThemeColors.secondry
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.selected_location),
                            style = AfaqTypography.semiBold16,
                            color = AfaqThemeColors.textBlack
                        )
                        Text(
                            text = locationName,
                            style = AfaqTypography.regular14,
                            color = AfaqThemeColors.textSecondary,

                            )
                    }
                }
            }
            onLocationSelected(point.latitude, point.longitude)
        }
    }
}
