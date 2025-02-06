package com.sevban.detail.mapper

import com.sevban.common.extensions.EMPTY
import com.sevban.common.extensions.toTitleCase
import com.sevban.model.Forecast
import com.sevban.ui.model.ForecastWeatherUi
import com.sevban.ui.model.createWeatherIconURL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class ForecastUiModel(
    val cod: String?,
    val city: String?,
    val eachDayWithAverage: List<EachDayWithAverage>,
    val today: List<ForecastWeatherUi>,
)

data class EachDayWithAverage(
    val temperature: Int,
    val localDate: LocalDate,
    val dateAsDay: String,
    val icon: String,
    val description: String
)

fun Forecast.toForecastUiModel(): ForecastUiModel {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val tempGroupedByDay = temp.dropWhile {
        val parsed = LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
        val today = LocalDate.now()
        parsed == today
    }.groupBy {
        LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
    }

    val otherDaysForecastMap = tempGroupedByDay.map {
        val weatherList = it.value
        val average =
            weatherList.mapNotNull { forecastWeather -> forecastWeather.temperature }.average()
        EachDayWithAverage(
            temperature = average.roundToInt(),
            localDate = it.key,
            dateAsDay = it.key.dayOfWeek.name,
            icon = createWeatherIconURL(weatherList.first().icon ?: String.EMPTY),
            description = weatherList.first().description?.toTitleCase() ?: String.EMPTY
        )
    }

    val today = temp.takeWhile {
        val parsed = LocalDateTime.parse(it.date, dateFormatter).toLocalDate()
        val today = LocalDate.now()
        parsed == today
    }.map {
        ForecastWeatherUi(
            date = LocalDateTime.parse(it.date, dateFormatter).format(timeFormatter),
            icon = createWeatherIconURL(it.icon ?: String.EMPTY),
            description = it.description?.toTitleCase() ?: String.EMPTY,
            temperature = it.temperature?.roundToInt() ?: 0
        )
    }

    return ForecastUiModel(
        cod = cod,
        city = city,
        eachDayWithAverage = otherDaysForecastMap,
        today = today
    )
}