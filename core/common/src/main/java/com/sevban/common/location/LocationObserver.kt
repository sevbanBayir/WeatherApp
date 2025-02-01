package com.sevban.common.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import androidx.core.content.getSystemService
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
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
                    Priority.PRIORITY_HIGH_ACCURACY,
                    interval
                ).build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            println("Realtime Location: $location")
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

    fun getCurrentLocation() = callbackFlow {

        val hasFineLocationPermission = context.hasFineLocationPermission()
        val hasCoarseLocationPermission = context.hasCoarseLocationPermission()

        if (hasCoarseLocationPermission && hasFineLocationPermission) {

            val currentLocationRequest = CurrentLocationRequest.Builder()
                .setDurationMillis(5000)
                .setMaxUpdateAgeMillis(60000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()


            client.getCurrentLocation(currentLocationRequest, null)
                .addOnSuccessListener {
                    println("Current Location: $it.")
                    it?.let {
                        trySend(it)
                    }
                }
        } else {
            println("Current Location Error: ")

            close(MissingLocationPermissionException())
        }

        awaitClose()
    }

    private fun requestForActivatingLocationFeature(
        locationRequest: LocationRequest,
        onEnabled: () -> Unit,
    ) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            onEnabled()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(context as Activity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 100
    }

}