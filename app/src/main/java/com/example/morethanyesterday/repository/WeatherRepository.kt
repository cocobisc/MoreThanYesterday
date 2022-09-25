package com.example.morethanyesterday.repository

import com.example.morethanyesterday.data.Response
import com.example.morethanyesterday.data.remote.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        dt: Long
    ): Response<WeatherResponse?>
}