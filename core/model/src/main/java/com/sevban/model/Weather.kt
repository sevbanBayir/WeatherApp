package com.sevban.model

data class Weather(
    val id: Int,
    val description: String?,
    val icon: String?,
    val cityName: String?,
    val feelsLike: Double?,
    val grndLevel: Int?,
    val humidity: Int?,
    val pressure: Int?,
    val seaLevel: Int?,
    val temp: Double?,
    val tempMax: Double?,
    val tempMin: Double?,
    val visibility: Int?,
    val windSpeed: Double?,
)