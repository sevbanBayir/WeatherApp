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
    ): Flow<ForecastUiModel> = repository.getLocationForecast(lat, long).map { forecast ->
        forecast.toForecastUiModel()
    }
}

data class ForecastUiModel(
    val cod: String?,
    val city: String?,
    val temperaturesBy5Days: List<List<Int>>,
    val hoursBy5Days: List<List<Int>>
)

fun Forecast.toForecastUiModel(): ForecastUiModel {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val tempGroupedByDay = temp.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }

    return ForecastUiModel(
        cod = cod,
        city = city,
        temperaturesBy5Days = tempGroupedByDay.map {
            it.value.map {
                it.temperature?.toInt() ?: 0
            }
        },
        hoursBy5Days = tempGroupedByDay.map {
            it.value.map {
                LocalDateTime.parse(it.date, dateFormatter).hour
            }
        }
    )
}
