package com.example.morethanyesterday.di

import android.app.AlarmManager
import android.content.Context
import com.example.morethanyesterday.alarm.WeatherAlarmManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {
    @Provides
    @Singleton
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): WeatherAlarmManager =
        WeatherAlarmManager(context)
}