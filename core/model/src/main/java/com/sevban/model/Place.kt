package com.sevban.model

data class Place(
    val cityName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)

val Place.fullText: String get() = "$cityName, $country"

