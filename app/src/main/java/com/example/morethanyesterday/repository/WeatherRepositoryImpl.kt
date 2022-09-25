package com.example.morethanyesterday.repository

import com.example.morethanyesterday.data.remote.WeatherRemoteDataSource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        dt: Long
    ) = weatherRemoteDataSource.getWeather(latitude, longitude, dt)
}