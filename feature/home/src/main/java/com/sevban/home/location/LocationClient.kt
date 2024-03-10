package com.sevban.home.location

import android.location.Location

interface LocationClient {
    fun getLastKnownLocation(onLocationUpdated: (Location) -> Unit)
}