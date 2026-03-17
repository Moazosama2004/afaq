package com.example.afaq.data.favourite.datasource.local

import com.example.afaq.data.db.FavouriteDao
import com.example.afaq.data.local.db.FavouriteEntity
import kotlinx.coroutines.flow.Flow

class FavouriteLocalDataSource(
    private val favouriteDao: FavouriteDao
) {
    suspend fun insertFavourite(favourite: FavouriteEntity) {
        favouriteDao.insertFavourite(favourite)
    }

    suspend fun deleteFavourite(favourite: FavouriteEntity) {
        favouriteDao.deleteFavourite(favourite)
    }

    fun getAllFavourites(): Flow<List<FavouriteEntity>> {
        return favouriteDao.getAllFavourites()
    }

    suspend fun deleteFavouriteById(id: Int) {
        favouriteDao.deleteFavouriteById(id)
    }

}