package com.example.morethanyesterday.utils

import com.example.morethanyesterday.R
import com.example.morethanyesterday.data.TemperatureVO
import com.example.morethanyesterday.data.WeatherVO
import com.example.morethanyesterday.ui.state.WeatherUiState
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

fun WeatherVO.convertToUiState(
    prevTemp: Float,
    currTemp: Float,
    address: String
) = WeatherUiState(
    title = getWeatherTitle(prevTemp, currTemp),
    address = address,
    iconName = weather[0].icon,
    prevTemp = getTempString(prevTemp),
    currTemp = getTempString(currTemp),
    date = getCurrentDate(),
    mainImageRes = getMainImageRes(prevTemp, currTemp)
)

fun getTempString(temp: Float) = "$temp°C"

fun getMainImageRes(
    prev: Float,
    curr: Float
): Int {
    val threshold = 2.0f
    val dailyTempDiff = curr - prev

    return if (dailyTempDiff <= threshold.absoluteValue) {
        R.drawable.normal
    } else if (dailyTempDiff < -threshold) {
        R.drawable.cat_down
    } else {
        R.drawable.cat_up
    }
}

fun getCurrentDate(): String {
    val date = Date(System.currentTimeMillis());
    val sdf = SimpleDateFormat("HH시 mm분 기준");

    return sdf.format(date);
}

fun getWeatherTitle(
    prev: Float,
    curr: Float
): String {
    val threshold = 2.0f
    val threshold2 = 6.0f
    val dailyTempDiff = curr - prev

    return if (dailyTempDiff <= threshold.absoluteValue) {
        "어제와\n비슷해요"
    } else if (dailyTempDiff < -threshold) {
        if (dailyTempDiff < -threshold2) "어제보다\n추워요"
        else "어제보다\n낮아요"
    } else {
        if (dailyTempDiff > threshold2) "어제보다\n더워요"
        else "어제보다\n높아요"
    }
}