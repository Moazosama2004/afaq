package com.example.afaq.data.home

import com.example.afaq.data.home.datasource.local.HomeLocalDataSource
import com.example.afaq.data.home.datasource.remote.HomeRemoteDataSource
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.model.Forecast
import com.example.afaq.data.model.ForecastItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class HomeRepoTest {

    @MockK
    private lateinit var remoteDataSource: HomeRemoteDataSource

    @MockK
    private lateinit var localDataSource: HomeLocalDataSource
    private lateinit var networkUtils: FakeNetworkUtils
    private lateinit var repo: HomeRepo


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        networkUtils = FakeNetworkUtils()
        repo = HomeRepo(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            networkUtils = networkUtils
        )
    }


    @Test
    fun getCurrentWeather_whenLocationIfUserIsOnline_returnsWeatherFromAPI() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val fakeWeather = createDummyWeather()

        coEvery {
            remoteDataSource.getCurrentWeather(
                lat,
                lon,
                "metric",
                "en"
            )
        } returns Result.success(fakeWeather)
        coEvery { localDataSource.saveWeather(lat, lon, any()) } just runs
        //Act
        val result = repo.getCurrentWeather(lat, lon, "metric", "en")

        //Assert
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull()?.cityName, `is`("Cairo"))
    }


    @Test
    fun getCurrentWeather_whenLocationIfUserIsOffline_getCachedData() = runTest {
        // Arrange
        networkUtils.setOffline()
        val lat = 30.0444
        val lon = 31.2357
        val fakeWeather = createDummyWeather()

        coEvery { localDataSource.getCachedWeather(lat, lon) } returns fakeWeather
        coEvery { localDataSource.saveWeather(lat, lon, any()) } just runs
        //Act
        val result = repo.getCurrentWeather(lat, lon, "metric", "en")

        //Assert
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull()?.cityName, `is`("Cairo"))
    }

    @Test
    fun getForecast_whenLocationIfUserIsOnline_returnsForecastWeatherFromAPI() = runTest {
        // Arrange
        val lat = 30.0444
        val lon = 31.2357
        val forecast = createDummyForecast()

        coEvery { remoteDataSource.getForecast(lat, lon, "metric", "en") } returns Result.success(
            forecast
        )
        coEvery { localDataSource.saveForecast(lat, lon, any()) } just runs
        //Act
        val result = repo.getForecast(lat, lon, "metric", "en")

        //Assert
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull()?.cityName, `is`("Cairo"))
        assertThat(result.getOrNull()?.items?.size ?: 0, `is`(2))
    }

    @Test
    fun getForecast_whenLocationIfUserIsOffline_returnsForecastWeatherFromAPI() = runTest {
        // Arrange
        networkUtils.setOffline()
        val lat = 30.0444
        val lon = 31.2357
        val forecast = createDummyForecast()

        coEvery { localDataSource.getCachedForecast(lat, lon) } returns forecast

        //Act
        val result = repo.getForecast(lat, lon, "metric", "en")

        //Assert
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull()?.cityName, `is`("Cairo"))
        assertThat(result.getOrNull()?.items?.size ?: 0, `is`(2))
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