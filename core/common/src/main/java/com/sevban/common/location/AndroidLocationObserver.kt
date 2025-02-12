package com.sevban.common.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sevban.common.extensions.hasCoarseLocationPermission
import com.sevban.common.extensions.hasFineLocationPermission
import com.sevban.common.location.mapper.toDomainLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class AndroidLocationObserver @Inject constructor(
    private val context: Context,
) : LocationObserver {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation() = callbackFlow {

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
                    it?.let {
                        trySend(it.toDomainLocation())
                    }
                }
        } else {
            close(MissingLocationPermissionException())
        }

        awaitClose()
    }
}