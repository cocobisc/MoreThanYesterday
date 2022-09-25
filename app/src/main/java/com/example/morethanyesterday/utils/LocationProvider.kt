package com.example.morethanyesterday.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationProvider @Inject constructor(
    private val context: Context
) {

    fun getCurrentLocation(): Location? {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkRequiredPermissions(locationManager: LocationManager) =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    fun getGeoAddress(): Address? {
        val location = getCurrentLocation() ?: return null

        return Geocoder(context, Locale.KOREA).getFromLocation(location.latitude, location.longitude, 1)?.get(0)
    }
}