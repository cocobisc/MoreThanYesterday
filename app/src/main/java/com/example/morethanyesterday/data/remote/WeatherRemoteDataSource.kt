package com.example.morethanyesterday.data.remote

import com.example.morethanyesterday.data.Response
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getWeather(lat: Double, lon: Double, dt: Long): Response<WeatherResponse?> {
        val weatherDTO = weatherApi.getWeather(lat, lon, dt)

        if (weatherDTO != null) {
            return Response.Success(weatherDTO)
        }
        return Response.Fail(null, "날씨를 가져오는 데 실패했습니다.")
    }
}