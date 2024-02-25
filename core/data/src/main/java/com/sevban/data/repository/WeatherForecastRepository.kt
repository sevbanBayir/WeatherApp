package com.sevban.data.repository

import com.sevban.model.Forecast
import com.sevban.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherForecastRepository {
    fun getLocationWeather(
        lat: String,
        long: String
    ): Flow<Weather>

    fun getLocationForecast(
        lat: String,
        long: String
    ): Flow<Forecast>
}