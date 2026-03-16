package com.example.afaq.presentation.favourites.ui

import FavouritesUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.afaq.R
import com.example.afaq.data.db.AppDatabase
import com.example.afaq.data.favourite.FavouriteRepo
import com.example.afaq.data.favourite.datasource.local.FavouriteLocalDataSource
import com.example.afaq.data.favourite.datasource.remote.FavouriteRemoteDataSource
import com.example.afaq.data.network.RetroFitClient
import com.example.afaq.presentation.favourites.manager.AddFavouriteState
import com.example.afaq.presentation.favourites.manager.DeleteFavouriteState
import com.example.afaq.presentation.favourites.manager.FavouriteViewModel
import com.example.afaq.presentation.favourites.manager.FavouriteViewModelFactory
import com.example.afaq.presentation.theme.theme.AfaqColors
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.utils.NetworkUtils
import com.example.afaq.utils.getAppLocale
import kotlinx.coroutines.launch

@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    onCityClick: (Double, Double) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel = viewModel<FavouriteViewModel>(
        factory = remember {
            FavouriteViewModelFactory(
                FavouriteRepo(
                    favouriteRemoteDataSource = FavouriteRemoteDataSource(RetroFitClient.webApiService),
                    favouriteLocalDataSource = FavouriteLocalDataSource(
                        AppDatabase.getInstance(context).favouriteDao()
                    )
                )
            )
        }
    )

    var showMap by remember { mutableStateOf(false) }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lon by remember { mutableDoubleStateOf(0.0) }
    var isLocationSelected by remember { mutableStateOf(false) }
    var showOfflineDialog by remember { mutableStateOf(false) }

    val favouritesState by viewModel.favouritesState.collectAsState()
    val addState by viewModel.addState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    val snackbarMessage = stringResource(R.string.please_select_location)

    if (showOfflineDialog) {
        AlertDialog(
            onDismissRequest = { showOfflineDialog = false },
            containerColor = AfaqThemeColors.secondry,
            title = {
                Text(
                    text = stringResource(R.string.no_internet),
                    style = AfaqTypography.bold16,
                    color = AfaqThemeColors.textPrimary
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.offline_city_view_error),
                    style = AfaqTypography.regular14,
                    color = AfaqThemeColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = { showOfflineDialog = false }) {
                    Text(stringResource(R.string.ok), color = AfaqThemeColors.primary)
                }
            },
            icon = {
                Icon(Icons.Default.WifiOff, contentDescription = null, tint = AfaqColors.warning, modifier = Modifier.size(32.dp))
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AfaqThemeColors.background)
    ) {

        if (showMap) {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                onLocationSelected = { latitude, longitude ->
                    lat = latitude
                    lon = longitude
                    isLocationSelected = true
                },
                onBack = { 
                    showMap = false
                    isLocationSelected = false
                }
            )
        } else {
            when (favouritesState) {

                is FavouritesUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AfaqThemeColors.primary
                    )
                }

                is FavouritesUiState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AfaqThemeColors.primary,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = stringResource(R.string.no_favourites_yet),
                            style = AfaqTypography.bold20,
                            color = AfaqThemeColors.textPrimary
                        )
                        Text(
                            text = stringResource(R.string.tap_to_add_a_city),
                            style = AfaqTypography.regular14,
                            color = AfaqThemeColors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is FavouritesUiState.Success -> {
                    val favourites = (favouritesState as FavouritesUiState.Success).favourites
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(favourites) { entity ->
                            FavouriteCityCard(
                                favourite = entity,
                                onCardClick = {
                                    onCityClick(entity.lat, entity.lon)
                                },
                                onDeleteClick = {
                                    viewModel.deleteFavouriteById(entity.id)
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }

                is FavouritesUiState.Error -> {
                    Text(
                        text = (favouritesState as FavouritesUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }


        when (addState) {
            is AddFavouriteState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AfaqThemeColors.primary)
                }
            }

            is AddFavouriteState.Error -> {
                LaunchedEffect(addState) {
                    viewModel.resetAddState()
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = (addState as AddFavouriteState.Error).message,
                        color = Color.White,
                        style = AfaqTypography.regular14,
                        modifier = Modifier
                            .background(
                                color = AfaqColors.error,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            is AddFavouriteState.Success -> {
                LaunchedEffect(addState) {
                    showMap = false
                    isLocationSelected = false
                    viewModel.resetAddState()
                }
            }

            else -> {}
        }

        when (deleteState) {
            is DeleteFavouriteState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AfaqThemeColors.primary)
                }
            }

            is DeleteFavouriteState.Error -> {
                LaunchedEffect(deleteState) {
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = (deleteState as DeleteFavouriteState.Error).message,
                        color = Color.White,
                        style = AfaqTypography.regular14,
                        modifier = Modifier
                            .background(
                                color = AfaqColors.error,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            else -> {}
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 90.dp)
        )

        FloatingActionButton(
            onClick = {
                if (showMap) {
                    if (isLocationSelected) {
                        viewModel.addFavourite(lat, lon, getAppLocale().language)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarMessage)
                        }
                    }
                } else {
                    if (!NetworkUtils.isOnline(context)) {
                        showOfflineDialog = true
                    } else {
                        showMap = true
                        isLocationSelected = false
                    }
                }
            },
            containerColor = if (showMap && !isLocationSelected) AfaqThemeColors.surface.copy(alpha = 0.6f) else AfaqThemeColors.surface,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (showMap) Icons.Default.Check else Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}
