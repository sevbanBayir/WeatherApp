package com.sevban.common.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sevban.common.extensions.hasCoarseLocationPermission
import com.sevban.common.extensions.hasFineLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LocationObserver @Inject constructor(
    private val context: Context,
) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    fun observeLocation(interval: Long): Flow<Location> {
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
            val hasFineLocationPermission = context.hasFineLocationPermission()
            val hasCoarseLocationPermission = context.hasCoarseLocationPermission()

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
}