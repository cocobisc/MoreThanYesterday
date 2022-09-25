package com.example.morethanyesterday.di

import android.content.Context
import com.example.morethanyesterday.utils.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider =
        LocationProvider(context)
}