package com.example.afaq.presentation.favourites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
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
import com.example.afaq.data.home.HomeRepo
import com.example.afaq.data.home.datasource.local.HomeLocalDataSource
import com.example.afaq.data.home.datasource.local.WeatherDataStore
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.network.RetroFitClient
import com.example.afaq.presentation.home.manager.ForecastUiState
import com.example.afaq.presentation.home.manager.HomeViewModel
import com.example.afaq.presentation.home.manager.HomeViewModelFactory
import com.example.afaq.presentation.home.manager.WeatherUiState
import com.example.afaq.presentation.home.ui.DayDetailsGrid
import com.example.afaq.presentation.home.ui.ForecastWeekList
import com.example.afaq.presentation.home.ui.HourlyForecastRow
import com.example.afaq.presentation.home.ui.SectionHeader
import com.example.afaq.presentation.home.ui.WeatherCard
import com.example.afaq.presentation.settings.manager.SettingsViewModel
import com.example.afaq.presentation.theme.theme.AfaqThemeColors
import com.example.afaq.presentation.theme.theme.AfaqTypography
import com.example.afaq.data.network.NetworkUtils
import com.example.afaq.utils.getAppLocale
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteDetailsScreen(
    lat: Double,
    lon: Double,
    settingsViewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext
    val viewModel = viewModel<HomeViewModel>(
        factory = remember(context) {
            HomeViewModelFactory(
                HomeRepo(
                    remoteDataSource = HomeRemoteDataSource(RetroFitClient.webApiService),
                    localDataSource = HomeLocalDataSource(WeatherDataStore(context)),
                    networkUtils = NetworkUtils(context)
                )
            )
        }
    )

    val state by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()
    val tempUnit by settingsViewModel.tempUnit.collectAsState()
    val windSpeedUnit by settingsViewModel.windUnit.collectAsState()

    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L) // every 1 minute
            currentTime = System.currentTimeMillis()
        }
    }

    LaunchedEffect(lat, lon) {
        viewModel.loadWeather(
            lat = lat,
            lon = lon,
            lang = getAppLocale().language,
            units = "metric"
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AfaqThemeColors.background,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.day_details),
                        textAlign = TextAlign.Center,
                        style = AfaqTypography.semiBold24,
                        color = AfaqThemeColors.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AfaqThemeColors.primary
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(AfaqThemeColors.background)
        ) {
            when (state) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AfaqThemeColors.primary
                    )
                }
                is WeatherUiState.Success -> {
                    val weather = (state as WeatherUiState.Success).weather
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            WeatherCard(
                                weather = weather,
                                tempUnit = tempUnit,
                                currentTime = currentTime
                            )
                        }
                        item {
                            SectionHeader(title = stringResource(R.string.day_details))
                        }
                        item {
                            DayDetailsGrid(
                                weather = weather,
                                tempUnit = tempUnit,
                                windSpeedUnit = windSpeedUnit
                            )
                        }
                        item {
                            SectionHeader(title = stringResource(R.string.today_hourly))
                        }
                        item {
                            when (forecastState) {
                                is ForecastUiState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        color = AfaqThemeColors.primary
                                    )
                                }
                                is ForecastUiState.Success -> {
                                    val forecast = (forecastState as ForecastUiState.Success).forecast
                                    HourlyForecastRow(forecast = forecast, tempUnit = tempUnit)
                                }
                                is ForecastUiState.Error -> {
                                    Text(
                                        text = (forecastState as ForecastUiState.Error).message,
                                        color = Color.Red,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                        item {
                            SectionHeader(title = stringResource(R.string._5_day_forecast))
                        }
                        item {
                            when (forecastState) {
                                is ForecastUiState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(150.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = AfaqThemeColors.primary)
                                    }
                                }
                                is ForecastUiState.Success -> {
                                    val forecast = (forecastState as ForecastUiState.Success).forecast
                                    ForecastWeekList(forecast = forecast, tempUnit = tempUnit)
                                }
                                is ForecastUiState.Error -> {
                                    Text(
                                        text = (forecastState as ForecastUiState.Error).message,
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
                is WeatherUiState.Error -> {
                    Text(
                        text = (state as WeatherUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
