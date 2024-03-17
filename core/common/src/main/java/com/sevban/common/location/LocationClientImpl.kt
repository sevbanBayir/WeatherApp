package com.sevban.common.location

import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.sevban.common.extensions.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject


class LocationClientImpl @Inject constructor(
    private val context: Context
) : LocationClient {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(
        anyOf = [android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    override fun getLastKnownLocation(): Flow<Location?> = callbackFlow {
        if (context.hasLocationPermission().not()) throw MissingLocationPermissionException()

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            trySend(it)
        }.addOnFailureListener {
            trySend(null)
        }

        awaitClose()
    }
}

class MissingLocationPermissionException : Exception()