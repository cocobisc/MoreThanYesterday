package com.example.morethanyesterday.data

data class MetaWeatherVO(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherVO(
    val dt: Long,
    val temp: Float,
    val clouds: Int,
    val weather: List<MetaWeatherVO>
)

data class TemperatureVO(
    val prevTemp: Float,
    val currTemp: Float
)