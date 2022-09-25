package com.example.morethanyesterday.data.remote

import com.example.morethanyesterday.data.WeatherVO

data class WeatherResponse(
    val timezone: String? = null,
    val current: WeatherVO? = null
)