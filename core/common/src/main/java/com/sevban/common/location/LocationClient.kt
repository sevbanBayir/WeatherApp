package com.sevban.common.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLastKnownLocation(): Flow<Location>
    fun getLocationUpdates(interval: Long): Flow<Location>
    fun getCurrentLocation(): Flow<Location>
}