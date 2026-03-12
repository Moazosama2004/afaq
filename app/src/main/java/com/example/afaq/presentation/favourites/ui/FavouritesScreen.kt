package com.example.afaq.presentation.favourites.ui

import FavouritesUiState
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.afaq.data.favourite.FavouriteRepo
import com.example.afaq.data.favourite.datasource.local.FavouriteLocalDataSource
import com.example.afaq.data.favourite.datasource.remote.FavouriteRemoteDataSource
import com.example.afaq.db.AppDatabase
import com.example.afaq.presentation.favourites.manager.FavouriteViewModel
import com.example.afaq.presentation.favourites.manager.FavouriteViewModelFactory
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography

@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val viewModel = viewModel<FavouriteViewModel>(
        factory = remember {
            FavouriteViewModelFactory(
                FavouriteRepo(
                    favouriteRemoteDataSource = FavouriteRemoteDataSource, // ← fixed
                    favouriteLocalDataSource = FavouriteLocalDataSource(
                        AppDatabase.getInstance(context).favouriteDao()
                    )
                )
            )
        }
    )

    var showMap by remember { mutableStateOf(false) }
    var lat by remember { mutableStateOf(0.0) }
    var lon by remember { mutableStateOf(0.0) }

    val favouritesState by viewModel.favouritesState.collectAsState()

    Box( // ← Box instead of Scaffold
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
                },
                onBack = { showMap = false }
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
                            text = "No Favourites Yet",
                            style = AfaqTypography.bold20,
                            color = AfaqThemeColors.textPrimary
                        )
                        Text(
                            text = "Tap + to add a city",
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
                                onDeleteClick = {
                                    Log.d("delete", entity.toString())
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

        // FAB - always on top
        FloatingActionButton(
            onClick = {
                if (showMap) {
                    showMap = false
                    viewModel.addFavourite(lat, lon)
                    Log.d("point", "lat: $lat, lon: $lon")
                } else {
                    showMap = true
                }
            },
            containerColor = AfaqThemeColors.primary,
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