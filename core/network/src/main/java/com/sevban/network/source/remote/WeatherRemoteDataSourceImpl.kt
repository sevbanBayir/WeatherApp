package com.sevban.network.source.remote

import com.sevban.network.model.forecast.ForecastDTO
import com.sevban.network.model.weather.WeatherDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: RetrofitService
) : WeatherRemoteDataSource {
    override fun getLocationWeather(lat: String, long: String): Flow<Response<WeatherDTO>> = flow {
        emit(weatherApiService.getLocationWeather(lat = lat, long = long))
    }

    override fun getLocationForecast(lat: String, long: String): Flow<Response<ForecastDTO>> = flow {
        emit(weatherApiService.getLocationForecast(lat = lat, long = long))
    }
}