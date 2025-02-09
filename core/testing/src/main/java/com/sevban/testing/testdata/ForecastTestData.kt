package com.sevban.testing.testdata

import com.sevban.model.Forecast
import com.sevban.model.ForecastWeather

val dummyForecast = Forecast(
    cod = "200",
    city = "New York",
    cnt = 5,
    message = 0,
    temp = listOf(
        ForecastWeather(
            temperature = 12.5,
            date = "2025-02-08 12:00:00",
            icon = "04d",
            description = "Cloudy"
        ),
        ForecastWeather(
            temperature = 14.0,
            date = "2025-02-08 15:00:00",
            icon = "02d",
            description = "Partly Cloudy"
        ),
        ForecastWeather(
            temperature = 10.2,
            date = "2025-02-08 18:00:00",
            icon = "10d",
            description = "Rainy"
        ),
        ForecastWeather(
            temperature = 8.0,
            date = "2025-02-08 21:00:00",
            icon = "01n",
            description = "Clear"
        ),
        ForecastWeather(
            temperature = 6.5,
            date = "2025-02-09 00:00:00",
            icon = "01n",
            description = "Clear"
        )
    )
)