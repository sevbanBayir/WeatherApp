package com.sevban.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sevban.domain.usecase.ForecastUiModel
import com.sevban.home.components.forecastquadrant.LineChart

@Composable
fun ForecastContent(
    forecast: ForecastUiModel
) {
    LineChart(
        yAxisData = forecast.temperaturesBy5Days.first(),
        xAxisData = forecast.hoursBy5Days.first()
    )
}