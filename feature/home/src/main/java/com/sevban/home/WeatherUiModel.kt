package com.sevban.home

import com.sevban.common.constants.Constants
import com.sevban.common.extensions.toTitleCase
import com.sevban.model.Weather
import com.sevban.ui.util.EMPTY

data class WeatherUiModel(
    val id: Int,
    val description: String,
    val iconUrl: String,
    val cityName: String,
    val feelsLike: String,
    val grndLevel: String,
    val humidity: String,
    val pressure: String,
    val seaLevel: String,
    val temp: String,
    val tempMax: String,
    val tempMin: String,
    val visibility: String,
    val windSpeed: String,
)

fun Weather.toWeatherUiModel() = WeatherUiModel(
    id = id,
    description = description?.toTitleCase() ?: String.EMPTY,
    iconUrl = createWeatherIconURL(icon = icon ?: String.EMPTY),
    cityName = cityName ?: String.EMPTY,
    feelsLike = feelsLike?.toInt().toString(),
    grndLevel = grndLevel?.toString() ?: String.EMPTY,
    humidity = humidity?.toString() ?: String.EMPTY,
    pressure = pressure?.toString() ?: String.EMPTY,
    seaLevel = seaLevel?.toString() ?: String.EMPTY,
    temp = temp?.toInt()?.toString() ?: String.EMPTY,
    tempMax = tempMax?.toString() ?: String.EMPTY,
    tempMin = tempMin?.toString() ?: String.EMPTY,
    visibility = visibility?.toString() ?: String.EMPTY,
    windSpeed = windSpeed?.toString() ?: String.EMPTY,
)

private fun createWeatherIconURL(icon: String): String {
    return Constants.WEATHER_IMAGE_BASE_URL + icon + Constants.IMAGE_RESOLUTION
}
