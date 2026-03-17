package com.example.afaq.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.afaq.data.local.db.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavourite(favourite: FavouriteEntity)

    @Delete
    suspend fun deleteFavourite(favourite: FavouriteEntity)

    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): Flow<List<FavouriteEntity>>

    @Query("SELECT * FROM favourites WHERE lat = :lat AND lon = :lon LIMIT 1")
    suspend fun getFavourite(lat: Double, lon: Double): FavouriteEntity?

    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun deleteFavouriteById(id: Int)

}