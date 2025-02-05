package com.sevban.home.mapper

import com.sevban.common.extensions.toTitleCase
import com.sevban.home.model.createWeatherIconURL
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
    val chartData: ChartData,
)

data class ChartData(
    val temperatures: List<Int>,
    val dateList: List<String>
)

data class ForecastWeatherUi(
    val temperature: Int,
    val date: String,
    val icon: String,
    val description: String
)

fun Forecast.toForecastUiModel(): ForecastUiModel {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val tempGroupedByDay = temp.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }

    val forecastBy3Hours = temp.map { forecastWeather ->
        ForecastWeatherUi(
            temperature = forecastWeather.temperature?.roundToInt() ?: 0,
            date = LocalDateTime.parse(forecastWeather.date, dateFormatter).format(timeFormatter),
            icon = createWeatherIconURL(forecastWeather.icon ?: ""),
            description = forecastWeather.description?.split(" ")?.joinToString(" ") { it.toTitleCase() }
                ?: ""
        )
    }

    val chartData = ChartData(
        temperatures = forecastBy3Hours.map { it.temperature }.take(8),
        dateList = forecastBy3Hours.map { it.date }.take(8)
    )
    val hoursBy5Days = tempGroupedByDay.map {
        it.value.map {
            LocalDateTime.parse(it.date, dateFormatter).hour
        }
    }
    val temperaturesBy5Days = tempGroupedByDay.map {
        it.value.map {
            it.temperature?.toInt() ?: 0
        }
    }
    return ForecastUiModel(
        cod = cod,
        city = city,

        temperaturesBy5Days = temperaturesBy5Days,
        hoursBy5Days = hoursBy5Days,
        forecastBy3Hours = forecastBy3Hours,
        chartData = chartData
    )
}