package com.example.morethanyesterday.data.remote

import com.example.morethanyesterday.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("onecall/timemachine")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("dt") dt: Long,
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): WeatherResponse?
}