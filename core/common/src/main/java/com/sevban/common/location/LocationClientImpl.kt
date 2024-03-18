package com.sevban.common.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sevban.common.constants.Constants.LOCATION_DURATION
import com.sevban.common.extensions.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

 /*   @RequiresPermission(
        anyOf = [android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    override fun getLocationUpdates(): Flow<Location?> {
        return callbackFlow {
            if (context.hasLocationPermission().not()) throw MissingLocationPermissionException()

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {

            }

            val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, LOCATION_DURATION)
                .setWaitForAccurateLocation(false)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        trySend(location).isSuccess
                    } ?: launch { send(null) }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }*/
}

class MissingLocationPermissionException : Exception()