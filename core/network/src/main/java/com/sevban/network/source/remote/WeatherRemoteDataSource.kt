package com.sevban.network.source.remote

import com.sevban.network.model.forecast.ForecastDTO
import com.sevban.network.model.weather.WeatherDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRemoteDataSource {
    fun getLocationWeather(
        lat: String,
        long: String
    ): Flow<Response<WeatherDTO>>

    fun getLocationForecast(
        lat: String,
        long: String
    ): Flow<Response<ForecastDTO>>
}