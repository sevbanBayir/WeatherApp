package com.sevban.ui.model

data class ForecastWeatherUi(
    val temperature: Int,
    val date: String,
    val icon: String,
    val description: String
)