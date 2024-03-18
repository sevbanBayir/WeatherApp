package com.sevban.common.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLastKnownLocation(): Flow<Location?>
//    fun getLocationUpdates(): Flow<Location?>
}