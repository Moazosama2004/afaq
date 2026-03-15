package com.example.afaq.data.local.db

import com.example.afaq.data.home.model.Weather

// Weather → FavouriteEntity
fun Weather.toFavouriteEntity(): FavouriteEntity {
    return FavouriteEntity(
        lat = lat,
        lon = lon,
        cityName = cityName,
        country = country,
        temperature = temperature,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        visibility = visibility,
        description = description,
        icon = icon,
        sunrise = sunrise,
        sunset = sunset,
        date = date.toLong(),
        lastUpdated = System.currentTimeMillis()
    )
}

// FavouriteEntity → Weather
fun FavouriteEntity.toWeather(): Weather {
    return Weather(
        cityName = cityName,
        country = country,
        temperature = temperature,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        visibility = visibility,
        description = description,
        icon = icon,
        sunrise = sunrise,
        sunset = sunset,
        date = date.toInt(),
        lat = lat,
        lon = lon
    )
}