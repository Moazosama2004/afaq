package com.example.afaq.data.location

import android.content.Context
import com.example.afaq.data.location.datasource.LocationDataSource
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val context: Context) {

    private val locationDataSource = LocationDataSource(context)

    fun getUserLocation(): Flow<Pair<Double, Double>> {
        return locationDataSource.getLocation()
    }
}