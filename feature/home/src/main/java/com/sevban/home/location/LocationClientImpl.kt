package com.sevban.home.location

import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class LocationClientImpl @Inject constructor(
    context: Context
) : LocationClient {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(
        anyOf = [android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    override fun getLastKnownLocation(onLocationUpdated: (Location) -> Unit) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            onLocationUpdated(it)
        }
    }
}