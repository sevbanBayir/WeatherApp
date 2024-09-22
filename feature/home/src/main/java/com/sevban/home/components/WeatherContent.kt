package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.home.WeatherUiModel

@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FeelsLikeCard(
            feelsLikeTemp = weather.feelsLike,
            currentTemp = weather.temp,
            weatherDescription = weather.description,
            weatherIconUrl = weather.iconUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        HumidityCard(
            wind = weather.windSpeed,
            humidity = weather.humidity,
            visibility = weather.visibility
        )
    }
}
