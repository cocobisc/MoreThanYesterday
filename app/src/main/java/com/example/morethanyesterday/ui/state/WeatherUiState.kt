package com.example.morethanyesterday.ui.state

import com.example.morethanyesterday.R
import com.example.morethanyesterday.data.TemperatureVO
import com.example.morethanyesterday.data.WeatherVO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

data class WeatherUiState(
    val title: String = "",
    val address: String = "",
    val iconName: String = "",
    val prevTemp: String = "",
    val currTemp: String = "",
//    val tempVO: TemperatureVO? = null,
    val mainImageRes: Int = -1,
    val date: String = ""
)


