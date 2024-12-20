package com.sevban.domain.usecase

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.model.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherForecastRepository
) {
    fun execute(
        lat: String,
        long: String
    ): Flow<Forecast> = repository.getLocationForecast(lat, long)
}
