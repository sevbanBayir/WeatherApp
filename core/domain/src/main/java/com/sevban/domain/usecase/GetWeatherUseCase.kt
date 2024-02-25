package com.sevban.domain.usecase

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.model.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherForecastRepository
) {
    fun execute(
        lat: String,
        long: String
    ): Flow<Weather> = repository.getLocationWeather(lat, long)
}