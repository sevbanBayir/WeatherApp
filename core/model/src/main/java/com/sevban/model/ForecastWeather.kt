package com.sevban.model

data class ForecastWeather(
    val temperature: Double?,
    val date: String,
    val icon: String?,
    val description: String?
)