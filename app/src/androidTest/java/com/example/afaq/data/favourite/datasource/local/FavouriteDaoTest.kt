package com.example.afaq.data.favourite.datasource.local

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.afaq.data.db.AppDatabase
import com.example.afaq.data.db.FavouriteDao
import com.example.afaq.data.local.db.FavouriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var favouriteDao: FavouriteDao

    @Before
    fun setup() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(
            application,
            AppDatabase::class.java
        ).build()
        favouriteDao = database.favouriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertFavourite_takesFavouriteItem() = runTest {
        // Arrange
        val favItem = createFavouriteEntity(1)

        // Act
        favouriteDao.insertFavourite(favItem)
        val result = favouriteDao.getAllFavourites().first()

        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result[0].cityName, `is`("Cairo"))
        assertThat(result[0].lat, `is`(30.0444))
    }

    @Test
    fun deleteFavourite_takesFavouriteItem() = runTest {
        // Arrange
        val favItem = createFavouriteEntity(1)

        // Act
        favouriteDao.insertFavourite(favItem)
        favouriteDao.deleteFavourite(favItem)
        val result = favouriteDao.getAllFavourites().first()

        // Assert
        assertThat(result.size, `is`(0))
    }


    @Test
    fun getAllFavourites_returnsFlowOfListFavourites() = runTest {
        // Arrange
        val favItem1 = createFavouriteEntity(1)
        val favItem2 = createFavouriteEntity(2)
        val favItem3 = createFavouriteEntity(3)

        // Act
        favouriteDao.insertFavourite(favItem1)
        favouriteDao.insertFavourite(favItem2)
        favouriteDao.insertFavourite(favItem3)
        val result = favouriteDao.getAllFavourites().first()

        // Assert
        assertThat(result.size, `is`(3))
        assertThat(result[0].cityName, `is`("Cairo"))
        assertThat(result[1].cityName, `is`("Cairo"))
        assertThat(result[2].cityName, `is`("Cairo"))
    }

    private fun createFavouriteEntity(id: Int): FavouriteEntity {
        return FavouriteEntity(
            id = id,
            lat = 30.0444,
            lon = 31.2357,
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
            date = 1710000000L,
            lastUpdated = System.currentTimeMillis()
        )
    }
}