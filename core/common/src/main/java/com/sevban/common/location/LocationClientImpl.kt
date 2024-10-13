package com.sevban.common.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.getSystemService
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.sevban.common.extensions.hasCoarseLocationPermission
import com.sevban.common.extensions.hasFineLocationPermission
import com.sevban.common.extensions.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")

class LocationClientImpl @Inject constructor(
    private val context: Context
) : LocationClient {
    private val client = LocationServices.getFusedLocationProviderClient(context)
    private val hasBothLocationPermission = context.hasLocationPermission()
    private val hasCoarseLocationPermission = context.hasCoarseLocationPermission()
    private val hasFineLocationPermission = context.hasFineLocationPermission()

    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!

            var isGpsEnabled = false
            var isNetworkEnabled = false

            while (!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnabled) {
                    delay(3000L)
                }
            }

            if (hasCoarseLocationPermission && hasFineLocationPermission) {
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    interval
                ).build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            trySend(location)
                        }

                    }
                }
                client.requestLocationUpdates(request, callback, context.mainLooper)

                awaitClose {
                    client.removeLocationUpdates(callback)
                }
            } else {
                close(MissingLocationPermissionException())
            }
        }
    }

    override fun getCurrentLocation(): Flow<Location> {
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!

            var isGpsEnabled = false
            var isNetworkEnabled = false

            while (!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnabled) {
                    delay(3000L)
                }
            }

            if (hasCoarseLocationPermission && hasFineLocationPermission) {
                val request = CurrentLocationRequest.Builder().build()

                client.getCurrentLocation(request, null).addOnSuccessListener {
                    trySend(it)
                }.addOnFailureListener {
                    close(it)
                }
                awaitClose()
            } else {
                close(MissingLocationPermissionException())
            }
        }
    }

    override fun getLastKnownLocation(): Flow<Location> = callbackFlow {
        if (!hasBothLocationPermission)
            close(MissingLocationPermissionException())

        client.lastLocation.addOnSuccessListener {
            trySend(it)
        }.addOnFailureListener {
            close(it)
        }

        awaitClose()
    }
}

class MissingLocationPermissionException : Exception()