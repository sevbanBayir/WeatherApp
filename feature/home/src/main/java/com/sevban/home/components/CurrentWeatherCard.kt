package com.sevban.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.home.components.videobg.VideoPlayer
import com.sevban.home.components.videobg.getVideoName
import com.sevban.home.components.videobg.toWeatherType
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.home.model.WeatherUiModel

@Composable
fun CurrentWeatherCard(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    lastFetchedTime: String,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxSize()) {
            weather.description.toWeatherType().getVideoName()?.let {
                VideoPlayer(
                    videoName = it,
                    modifier = Modifier.height(530.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                LocationAndFetTimeBox(
                    weather = weather,
                    lastFetchedTime = lastFetchedTime,
                    onLocationClick = onLocationClick
                )

                TemperatureContainer(weather = weather)

                WeatherFeaturesCard(weather = weather)

                Spacer(modifier = Modifier.height(16.dp))

                LineChartContainer(forecast = forecast)
            }
        }
    }
}