package com.example.afaq.data.favourite

import com.example.afaq.data.favourite.datasource.local.FavouriteLocalDataSource
import com.example.afaq.data.favourite.datasource.remote.FavouriteRemoteDataSource
import com.example.afaq.data.home.model.Weather
import com.example.afaq.data.local.db.FavouriteEntity
import com.example.afaq.data.model.Forecast
import kotlinx.coroutines.flow.Flow

class FavouriteRepo(
    private val favouriteRemoteDataSource: FavouriteRemoteDataSource  ,
    private val favouriteLocalDataSource: FavouriteLocalDataSource,
) {
    suspend fun insertFavourite(favourite: FavouriteEntity) {
        favouriteLocalDataSource.insertFavourite(favourite)
    }

    suspend fun deleteFavourite(favourite: FavouriteEntity) {
        favouriteLocalDataSource.deleteFavourite(favourite)
    }

    fun getAllFavourites() : Flow<List<FavouriteEntity>> {
        return favouriteLocalDataSource.getAllFavourites()
    }

    suspend fun deleteFavouriteById(id : Int) {
        favouriteLocalDataSource.deleteFavouriteById(id)
    }


    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ) : Result<Weather> {
        return favouriteRemoteDataSource.getCurrentWeather(
            lat,
            lon,
            apiKey,
            units,
            lang
        )
    }

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ) : Result<Forecast> {
        return favouriteRemoteDataSource.getForecast(
            lat,
            lon,
            apiKey,
            units,
            lang
        )
    }

}