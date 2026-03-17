package com.example.afaq.presentation.home.manager

import com.example.afaq.data.home.HomeRepo
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.data.model.ForecastItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


class HomeViewModelTest {

    @MockK
    private lateinit var repo: HomeRepo
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getCurrentWeather_takesCertainLocation_returnsSuccessState() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val fakeWeather = createDummyWeather()
        coEvery { repo.getCurrentWeather(lat, lon, "metric", "en") } returns Result.success(
            fakeWeather
        )

        // Act
        viewModel.getCurrentWeather(lat, lon, "metric", "en")

        advanceUntilIdle()

        // Assert

        assertThat(viewModel.weatherState.value, `is`(WeatherUiState.Success(fakeWeather)))

    }

    @Test
    fun getCurrentWeather_takesCertainLocation_returnsFailureState() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        createDummyWeather()
        coEvery { repo.getCurrentWeather(lat, lon, "metric", "en") } returns Result.failure(
            Exception("UnKnown error")
        )

        // Act
        viewModel.getCurrentWeather(lat, lon, "metric", "en")

        advanceUntilIdle()

        // Assert

        assertThat(viewModel.weatherState.value, `is`(WeatherUiState.Error("UnKnown error")))

    }

    @Test
    fun loadWeather_takesLocation_callsGetWeatherAndForecast() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val fakeWeather = createDummyWeather()
        val fakeForecast = createDummyForecast()

        coEvery {
            repo.getCurrentWeather(lat, lon, "metric", "en")
        } returns Result.success(fakeWeather)

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.success(fakeForecast)

        // Act
        viewModel.loadWeather(lat, lon)
        advanceUntilIdle()

        // Assert
        coVerify { repo.getCurrentWeather(lat, lon, "metric", "en") }
        coVerify { repo.getForecast(lat, lon, "metric", "en") }

        // Assert
        assertThat(viewModel.weatherState.value, `is`(WeatherUiState.Success(fakeWeather)))
        assertThat(viewModel.forecastState.value, `is`(ForecastUiState.Success(fakeForecast)))
    }

    @Test
    fun getForecast_takesCertainLocation_returnsSuccessState() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val fakeForecast = createDummyForecast()

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.success(fakeForecast)

        // Act
        viewModel.getForecast(lat, lon)
        advanceUntilIdle()

        // Assert
        assertThat(
            viewModel.forecastState.value,
            `is`(ForecastUiState.Success(fakeForecast))
        )
    }

    @Test
    fun getForecast_takesCertainLocation_returnsFailureState() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.failure(Exception("Unknown error"))

        // Act
        viewModel.getForecast(lat, lon)
        advanceUntilIdle()

        // Assert
        assertThat(
            viewModel.forecastState.value,
            `is`(ForecastUiState.Error("Unknown error"))
        )
    }

    @Test
    fun getForecast_initially_emitsLoadingState() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } coAnswers {
            delay(1000L)
            Result.success(createDummyForecast())
        }

        // Act
        viewModel.getForecast(lat, lon)

        // Assert
        assertThat(
            viewModel.forecastState.value,
            `is`(ForecastUiState.Loading)
        )
        advanceUntilIdle()
    }

    @Test
    fun getForecast_callsRepoWithCorrectParams() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.success(createDummyForecast())

        // Act
        viewModel.getForecast(lat, lon, "metric", "en")
        advanceUntilIdle()

        // Assert
        coVerify {
            repo.getForecast(lat, lon, "metric", "en") // ← correct params ✅
        }
    }

    @Test
    fun getForecast_callsRepoExactlyOnce() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.success(createDummyForecast())

        // Act
        viewModel.getForecast(lat, lon)
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) {
            repo.getForecast(lat, lon, "metric", "en") // ← called once ✅
        }
    }

    @Test
    fun getForecast_successState_hasCorrectItems() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val fakeForecast = createDummyForecast()

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.success(fakeForecast)

        // Act
        viewModel.getForecast(lat, lon)
        advanceUntilIdle()

        // Assert
        val state = viewModel.forecastState.value as ForecastUiState.Success
        assertThat(state.forecast.cityName, `is`("Cairo"))
        assertThat(state.forecast.items.size, `is`(2))
    }

    @Test
    fun getForecast_errorState_hasCorrectMessage() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357

        coEvery {
            repo.getForecast(lat, lon, "metric", "en")
        } returns Result.failure(Exception("No internet and no cached data"))

        // Act
        viewModel.getForecast(lat, lon)
        advanceUntilIdle()

        // Assert
        val state = viewModel.forecastState.value as ForecastUiState.Error
        assertThat(state.message, `is`("No internet and no cached data"))
    }


    private fun createDummyWeather(): Weather {
        return Weather(
            cityName = "Cairo",
            country = "EG",
            temperature = 25.0,
            feelsLike = 23.0,
            tempMin = 20.0,
            tempMax = 30.0,
            humidity = 40,
            pressure = 1013,
            windSpeed = 5.0,
            visibility = 10000,
            description = "clear sky",
            icon = "01d",
            sunrise = 1710000000L,
            sunset = 1710043200L,
            date = 1710000000,
            lat = 30.0444,
            lon = 31.2357,
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun createDummyForecast(): Forecast {
        return Forecast(
            cityName = "Cairo",
            country = "EG",
            sunrise = 1710000000L,
            sunset = 1710043200L,
            items = listOf(
                ForecastItem(
                    dt = 1710000000L,
                    dtTxt = "2024-03-10 12:00:00",
                    temp = 25.0,
                    feelsLike = 23.0,
                    tempMin = 20.0,
                    tempMax = 30.0,
                    humidity = 40,
                    pressure = 1013,
                    windSpeed = 5.0,
                    visibility = 10000,
                    description = "clear sky",
                    icon = "01d",
                    pop = 0.0
                ),
                ForecastItem(
                    dt = 1710010800L,
                    dtTxt = "2024-03-10 15:00:00",
                    temp = 28.0,
                    feelsLike = 26.0,
                    tempMin = 22.0,
                    tempMax = 32.0,
                    humidity = 35,
                    pressure = 1010,
                    windSpeed = 4.0,
                    visibility = 9000,
                    description = "few clouds",
                    icon = "02d",
                    pop = 0.1
                )
            )
        )
    }
}