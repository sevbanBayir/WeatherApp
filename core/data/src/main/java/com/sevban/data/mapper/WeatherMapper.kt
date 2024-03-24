package com.sevban.data.mapper

import com.sevban.model.Weather
import com.sevban.network.model.weather.WeatherDTO

fun WeatherDTO.toWeather() = Weather(
    id = id ?: -1,
    description = weather?.first()?.description,
    cityName = name,
    feelsLike = main?.feelsLike,
    grndLevel = main?.grndLevel,
    humidity = main?.humidity,
    pressure = main?.pressure,
    seaLevel = main?.seaLevel,
    temp = main?.temp,
    tempMax = main?.tempMax,
    tempMin = main?.tempMin,
    visibility = visibility,
    windSpeed = wind?.speed
)