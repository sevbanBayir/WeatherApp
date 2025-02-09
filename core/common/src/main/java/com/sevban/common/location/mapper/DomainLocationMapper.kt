package com.sevban.common.location.mapper

import android.location.Location
import com.sevban.model.DomainLocation

fun Location.toDomainLocation(): DomainLocation {
    return DomainLocation(
        latitude = latitude,
        longitude = longitude,
    )
}