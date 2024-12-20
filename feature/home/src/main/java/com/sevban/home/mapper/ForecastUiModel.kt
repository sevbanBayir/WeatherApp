package com.sevban.home.mapper

import com.sevban.model.Forecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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