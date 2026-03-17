package com.example.afaq.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lon: Double,
    val cityName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val visibility: Int,
    val description: String,
    val icon: String,
    val sunrise: Long,
    val sunset: Long,
    val date: Long,
    val lastUpdated: Long   // ← no default value
)