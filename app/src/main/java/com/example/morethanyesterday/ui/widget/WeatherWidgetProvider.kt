package com.example.morethanyesterday.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.example.morethanyesterday.usecase.GetWeatherUseCase
import com.example.morethanyesterday.utils.LocationProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidgetProvider : AppWidgetProvider() {
    @Inject
    lateinit var getWeatherUseCase: GetWeatherUseCase
    @Inject
    lateinit var locationProvider: LocationProvider

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {

        }
    }
}