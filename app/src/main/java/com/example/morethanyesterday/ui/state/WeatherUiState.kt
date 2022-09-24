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
    val tempVO: TemperatureVO? = null,
    val mainImageRes: Int = -1,
    val date: String = ""
)

fun WeatherVO.convertToUiState(
    prevTemp: Float,
    currTemp: Float,
    address: String
) = WeatherUiState(
    title = getWeatherTitle(prevTemp, currTemp),
    address = address,
    iconName = weather[0].icon,
    tempVO = TemperatureVO(prevTemp, currTemp),
    date = getCurrentDate(),
    mainImageRes = mapToMainImageRes(prevTemp, currTemp)
)

private fun mapToMainImageRes(
    prev: Float,
    curr: Float
) = if (prev < curr) {
        R.drawable.cat_up
    } else if (prev > curr) {
        R.drawable.cat_down
    } else {
        R.drawable.normal
    }

private fun getCurrentDate(): String {
    val date = Date(System.currentTimeMillis());
    val sdf = SimpleDateFormat("hh:mm");

    return sdf.format(date);
}

private fun getWeatherTitle(
    prev: Float,
    curr: Float
): String {
    val threshold = 2.0f
    val dailyTempDiff = curr - prev

    return if (dailyTempDiff <= threshold.absoluteValue) {
        "어제와\n비슷해요"
    } else if (dailyTempDiff < -threshold) {
        if (curr < 10) "어제보다\n시원해요"
        else "어제보다\n추워요"
    } else {
        if (curr < 20) "어제보다\n따듯해요"
        else "어제보다\n더워요"
    }
}
