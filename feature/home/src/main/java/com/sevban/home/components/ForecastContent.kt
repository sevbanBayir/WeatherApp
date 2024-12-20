package com.sevban.home.components

import androidx.compose.runtime.Composable
import com.sevban.home.components.forecastquadrant.LineChart
import com.sevban.home.mapper.ForecastUiModel

@Composable
fun ForecastContent(
    forecast: ForecastUiModel
) {
    LineChart(
        yAxisData = forecast.temperaturesBy5Days.first(),
        xAxisData = forecast.hoursBy5Days.first()
    )
}