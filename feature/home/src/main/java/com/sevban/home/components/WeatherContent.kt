package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sevban.home.model.WeatherUiModel
import com.sevban.home.mapper.ForecastUiModel

@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherCard(
            weather = weather,
            onLocationClick = onLocationClick,
            forecast = forecast
        )
    }
}
