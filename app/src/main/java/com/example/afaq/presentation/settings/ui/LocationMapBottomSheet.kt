package com.example.afaq.presentation.settings.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.afaq.R
import com.example.afaq.presentation.theme.theme.AfaqColors
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.presentation.theme.theme.LocalIsDarkTheme
import com.example.afaq.utils.getAddressFromLocation
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMapBottomSheet(
    onDismiss: () -> Unit,
    onLocationSelected: (lat: Double, lon: Double) -> Unit
) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false }
    )
    val isDarkTheme = LocalIsDarkTheme.current

    var selectedLat by remember { mutableStateOf<Double?>(null) }
    var selectedLon by remember { mutableStateOf<Double?>(null) }
    var locationName by remember { mutableStateOf("") }
    var marker by remember { mutableStateOf<Marker?>(null) }

    LaunchedEffect(selectedLat, selectedLon) {
        if (selectedLat != null && selectedLon != null) {
            locationName = withContext(Dispatchers.IO) {
                getAddressFromLocation(context, selectedLat!!, selectedLon!!)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = sheetState,
        containerColor = AfaqThemeColors.dialog,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxHeight(0.85f),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 8.dp)
        ) {

            Text(
                text = stringResource(R.string.selected_location),
                style = AfaqTypography.bold20,
                color = AfaqThemeColors.textPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (selectedLat != null && selectedLon != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AfaqThemeColors.secondry
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = AfaqColors.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = locationName.ifEmpty { "..." },
                                style = AfaqTypography.semiBold14,
                                color = AfaqThemeColors.textPrimary
                            )
                            Text(
                                text = "${"%.4f".format(selectedLat)}, ${"%.4f".format(selectedLon)}",
                                style = AfaqTypography.regular12,
                                color = AfaqThemeColors.textSecondary
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Tap on the map to select your location",
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AndroidView(
                factory = { ctx ->
                    Configuration.getInstance().userAgentValue = ctx.packageName
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(6.0)
                        controller.setCenter(GeoPoint(26.8206, 30.8025))

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
                                selectedLat = p.latitude
                                selectedLon = p.longitude

                                marker?.let { overlays.remove(it) }

                                val newMarker = Marker(this@apply).apply {
                                    position = p
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title =
                                        "${"%.4f".format(p.latitude)}, ${"%.4f".format(p.longitude)}"
                                }
                                marker = newMarker
                                overlays.add(newMarker)
                                invalidate()
                                return true
                            }

                            override fun longPressHelper(p: GeoPoint): Boolean = false
                        })
                        overlays.add(eventsOverlay)
                    }
                },
                update = { view ->
                    if (isDarkTheme) {
                        val matrix = ColorMatrix(
                            floatArrayOf(
                                -1.0f, 0.0f, 0.0f, 0.0f, 255f,
                                0.0f, -1.0f, 0.0f, 0.0f, 255f,
                                0.0f, 0.0f, -1.0f, 0.0f, 255f,
                                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                            )
                        )
                        view.overlayManager.tilesOverlay.setColorFilter(
                            ColorMatrixColorFilter(
                                matrix
                            )
                        )
                    } else {
                        view.overlayManager.tilesOverlay.setColorFilter(null)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (selectedLat != null && selectedLon != null) {
                            onLocationSelected(selectedLat!!, selectedLon!!)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedLat != null && selectedLon != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AfaqColors.primary,
                        disabledContainerColor = AfaqColors.primary.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "Confirm",
                        color = Color.White,
                        style = AfaqTypography.semiBold14,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color.White, style = AfaqTypography.semiBold14)
                }
            }
        }
    }
}
