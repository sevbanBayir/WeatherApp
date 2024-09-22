package com.sevban.home.components

import androidx.compose.runtime.Composable
import com.sevban.home.components.forecastquadrant.LineChart
import com.sevban.model.Forecast

@Composable
fun ForecastContent(
    forecast: Forecast
) {
    LineChart(yAxisData = forecast.temp.take(8).map { it.temperature?.toInt()!! })
}