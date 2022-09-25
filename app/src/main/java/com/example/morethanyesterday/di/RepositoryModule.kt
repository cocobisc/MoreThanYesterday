package com.example.morethanyesterday.di

import com.example.morethanyesterday.data.remote.WeatherApi
import com.example.morethanyesterday.data.remote.WeatherRemoteDataSource
import com.example.morethanyesterday.repository.WeatherRepository
import com.example.morethanyesterday.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRemoteDataSource(
        weatherApi: WeatherApi
    ): WeatherRemoteDataSource =
        WeatherRemoteDataSource(weatherApi)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherRemoteDataSource: WeatherRemoteDataSource
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherRemoteDataSource)
}