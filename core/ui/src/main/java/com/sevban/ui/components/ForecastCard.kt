package com.sevban.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sevban.ui.model.ForecastWeatherUi

@Composable
fun ForecastCard(
    modifier: Modifier = Modifier,
    forecastWeatherUi: ForecastWeatherUi
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Spacer(Modifier.height(4.dp))
        Text(text = forecastWeatherUi.date)
        AsyncImage(
            model = forecastWeatherUi.icon,
            contentDescription = forecastWeatherUi.description
        )
        Text(text = forecastWeatherUi.description)
        Text(text = "${forecastWeatherUi.temperature} ֯")
        Spacer(Modifier.height(4.dp))
    }
}
