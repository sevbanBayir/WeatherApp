package com.sevban.home.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toTitleCase
import com.sevban.model.Forecast
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

data class ForecastUiModel(
    val cod: String?,
    val city: String?,
    val temperaturesBy5Days: List<List<Int>>,
    val hoursBy5Days: List<List<Int>>,
    val forecastBy3Hours: List<ForecastWeatherUi>,
    val chartData: ChartData,
    val next24Hours: List<ForecastWeatherUi>
)

fun Forecast.nex24Hours(): List<ForecastWeatherUi> {
    return temp.takeWhile {
        val now = LocalDateTime.now()
        val date = LocalDateTime.parse(it.date, dateFormatter)
        date < now.plusHours(24)
    }.map {
        ForecastWeatherUi(
            temperature = it.temperature?.roundToInt() ?: 0,
            date = LocalDateTime.parse(it.date, dateFormatter).format(DateTimeFormatter.ofPattern("HH:mm")),
            icon = createWeatherIconURL(it.icon ?: String.EMPTY),
            description = it.description?.toTitleCase() ?: String.EMPTY
        )
    }
}

data class ChartData(
    val temperatures: List<Int>,
    val dateList: List<String>
)

fun Forecast.toForecastUiModel(): ForecastUiModel {

    val tempGroupedByDay = temp.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }

    val forecastBy3Hours = temp.map { forecastWeather ->
        ForecastWeatherUi(
            temperature = forecastWeather.temperature?.roundToInt() ?: 0,
            date = LocalDateTime.parse(forecastWeather.date, dateFormatter).format(timeFormatter),
            icon = createWeatherIconURL(forecastWeather.icon ?: String.EMPTY),
            description = forecastWeather.description?.toTitleCase() ?: String.EMPTY
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
        chartData = chartData,
        next24Hours = nex24Hours()
    )
}