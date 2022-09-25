package com.example.morethanyesterday.usecase

import com.example.morethanyesterday.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, dt: Long) =
        repository.getWeather(lat, lon, dt)
}