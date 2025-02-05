package com.sevban.home.mapper

import com.sevban.model.Forecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class ForecastUiModel(
    val cod: String?,
    val city: String?,
    val temperaturesBy5Days: List<List<Int>>,
    val hoursBy5Days: List<List<Int>>,
    val forecastBy3Hours: List<ForecastWeatherUi>,
    val chartData: ChartData
)

data class ChartData(
    val temperatures: List<Int>,
    val dateList: List<String>
)

data class ForecastWeatherUi(
    val temperature: Int,
    val date: String
)

fun Forecast.toForecastUiModel(): ForecastUiModel {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val tempGroupedByDay = temp.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }
    val forecastBy3Hours = temp.map {
        ForecastWeatherUi(
            temperature = it.temperature?.roundToInt() ?: 0,
            date = LocalDateTime.parse(it.date, dateFormatter).format(timeFormatter)
        )
    }
    println(forecastBy3Hours)

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
        },
        forecastBy3Hours = forecastBy3Hours,
        chartData = ChartData(
            temperatures = forecastBy3Hours.map { it.temperature }.take(8),
            dateList = forecastBy3Hours.map { it.date }.take(8)
        )
    )
}