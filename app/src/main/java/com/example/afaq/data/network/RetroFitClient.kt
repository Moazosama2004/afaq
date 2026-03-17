package com.example.afaq.data.network

import com.example.afaq.data.home.datasource.remote.WeatherApiService
import com.example.afaq.utils.AuthInterceptor
import com.example.afaq.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {

    val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()

    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    // Services
    val webApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)

    }
}