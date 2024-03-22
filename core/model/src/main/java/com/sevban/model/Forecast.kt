package com.sevban.model

data class Forecast(
    val cod: String?,
    val city: String?,
    val cnt: Int?,
    val message: Int?,
    val temp: List<Double?>
)