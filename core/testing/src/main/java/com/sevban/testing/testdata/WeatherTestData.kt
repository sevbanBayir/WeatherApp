package com.sevban.testing.testdata

import com.sevban.model.Weather

val dummyWeather = Weather(
    id = 800, // Clear sky
    description = "clear sky",
    icon = "01d", // Daytime clear sky icon
    cityName = "New York",
    feelsLike = 23.5,
    grndLevel = 1012,
    humidity = 60,
    pressure = 1015,
    seaLevel = 1015,
    temp = 24.0,
    tempMax = 26.0,
    tempMin = 22.0,
    visibility = 10000, // 10 km visibility
    windSpeed = 3.5
)