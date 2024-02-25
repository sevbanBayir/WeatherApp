package com.sevban.data.repository

import com.sevban.data.mapper.toForecast
import com.sevban.data.mapper.toWeather
import com.sevban.model.Forecast
import com.sevban.model.Weather
import com.sevban.network.model.forecast.ForecastDTO
import com.sevban.network.model.weather.WeatherDTO
import com.sevban.network.source.remote.WeatherRemoteDataSource
import com.sevban.network.util.asRestApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherForecastRepositoryImpl(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherForecastRepository {
    override fun getLocationWeather(lat: String, long: String): Flow<Weather> =
        weatherRemoteDataSource.getLocationWeather(lat, long).asRestApiCall(WeatherDTO::toWeather)


    override fun getLocationForecast(lat: String, long: String): Flow<Forecast> =
        weatherRemoteDataSource.getLocationForecast(lat, long).asRestApiCall(ForecastDTO::toForecast)

}