package com.example.morethanyesterday.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.morethanyesterday.R
import com.example.morethanyesterday.data.Response
import com.example.morethanyesterday.data.WeatherVO
import com.example.morethanyesterday.ui.MainActivity
import com.example.morethanyesterday.usecase.GetWeatherUseCase
import com.example.morethanyesterday.utils.LocationProvider
import com.example.morethanyesterday.utils.getCurrentDate
import com.example.morethanyesterday.utils.getMainImageRes
import com.example.morethanyesterday.utils.getWeatherTitle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class WeatherWidgetProvider : AppWidgetProvider() {
    @Inject
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @Inject
    lateinit var locationProvider: LocationProvider

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            context?.let {
                updateAppWidget(
                    it,
                    AppWidgetManager.getInstance(context),
                    intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
                )
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        GlobalScope.launch {
            get2WeathersToCompare(context, appWidgetManager, appWidgetId)
        }
    }

    private suspend fun get2WeathersToCompare(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
        val currentLocation = locationProvider.getCurrentLocation()
        val currentTime = System.currentTimeMillis() / 1000
        val prevTime = currentTime - 86400

        currentLocation?.let {
            getWeatherUseCase.invoke(it.latitude, it.longitude, prevTime).let { prevResponse ->
                if (prevResponse is Response.Fail) {
                    return
                }
                getWeatherUseCase.invoke(it.latitude, it.longitude, currentTime)
                    .also { currResponse ->
                        when (currResponse) {
                            is Response.Success -> {
                                onSuccess(
                                    context,
                                    appWidgetManager,
                                    appWidgetId,
                                    prevResponse.data!!.current!!,
                                    currResponse.data!!.current!!,
                                )
                            }
                            is Response.Fail -> {
                                return
                            }
                        }
                    }
            }
        }
    }

    private fun onSuccess(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        prevWeatherVO: WeatherVO,
        currWeatherVO: WeatherVO,
    ) {
        val moveToMainPendingIntent: PendingIntent = Intent(context, MainActivity::class.java).let { intent ->
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val refreshWidgetPendingIntent: PendingIntent = Intent(context, WeatherWidgetProvider::class.java).let { intent ->
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        val prevTemp = prevWeatherVO.temp
        val currTemp = currWeatherVO.temp

        RemoteViews(context.packageName, R.layout.weather_widget).apply {
            setImageViewResource(R.id.widget_main_image, getMainImageRes(prevTemp, currTemp))
            setTextViewText(R.id.widget_weather_title, getWeatherTitle(prevTemp, currTemp))
            setTextViewText(R.id.widget_date, getCurrentDate())
            setOnClickPendingIntent(R.id.widget_root, moveToMainPendingIntent)
            setOnClickPendingIntent(R.id.widget_refresh, refreshWidgetPendingIntent)
        }.also {
            appWidgetManager.updateAppWidget(appWidgetId, it)
        }
    }
}