package com.example.morethanyesterday.ui.event

sealed interface WeatherUiEvent {
    object Refresh: WeatherUiEvent
}