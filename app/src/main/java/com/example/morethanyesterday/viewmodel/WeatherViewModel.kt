package com.example.morethanyesterday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morethanyesterday.data.Response
import com.example.morethanyesterday.data.WeatherVO
import com.example.morethanyesterday.ui.state.WeatherUiState
import com.example.morethanyesterday.usecase.GetWeatherUseCase
import com.example.morethanyesterday.utils.LocationProvider
import com.example.morethanyesterday.utils.convertToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val weatherUiState = _weatherUiState.asStateFlow()

    private val _toastMessage = Channel<String>()
    val toastMessage = _toastMessage.receiveAsFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()


    fun getWeather() {
        viewModelScope.launch {
            enterLoadingState {
                get2WeathersToCompare()
            }
        }
    }

    private suspend fun get2WeathersToCompare() {

        val currentLocation = locationProvider.getCurrentLocation()
        val currentTime = System.currentTimeMillis() / 1000
        val prevTime = currentTime - 86400

        currentLocation?.let {
            getWeatherUseCase.invoke(it.latitude, it.longitude, prevTime).let { prevResponse ->
                if (prevResponse is Response.Fail) {
                    onFail("날씨 데이터를 불러오는 데 실패했습니다.")
                    return
                }
                getWeatherUseCase.invoke(it.latitude, it.longitude, currentTime).also { currResponse ->
                    when (currResponse) {
                        is Response.Success -> {
                            onSuccess(prevResponse.data!!.current!!, currResponse.data!!.current!!, "날씨 정보 업데이트 성공")
                        }
                        is Response.Fail -> {
                            onFail("날씨 데이터를 불러오는 데 실패했습니다.")
                        }
                    }
                }
            }
        } ?: run {
            onFail("위치 정보를 불러오는 데 실패했습니다.")
        }
    }

    private suspend fun onSuccess(prevWeatherVO: WeatherVO, currWeatherVO: WeatherVO, message: String) {
        val currentAddress = locationProvider.getGeoAddress()?.let {
            it.adminArea + " " + it.subLocality + "\n" + it.thoroughfare}.toString()

        _weatherUiState.value = currWeatherVO.convertToUiState(
            prevWeatherVO.temp.toCelsius() ,
            currWeatherVO.temp.toCelsius(),
            currentAddress
        )

        _toastMessage.send(message)
    }

    private fun Float.toCelsius() = ((this - 273.15f) * 10).roundToInt() / 10f

    private suspend fun onFail(message: String) {
        _toastMessage.send(message)
    }

    private inline fun enterLoadingState(block: () -> Unit) {
        _isLoading.value = true
        block()
        _isLoading.value = false
    }
}