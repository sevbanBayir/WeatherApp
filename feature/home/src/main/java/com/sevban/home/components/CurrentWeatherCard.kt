package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.R
import com.sevban.home.components.forecastquadrant.GraphStyle
import com.sevban.home.components.forecastquadrant.LineChart
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.home.model.WeatherUiModel

@Composable
fun CurrentWeatherCard(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    lastFetchedTime: String = "12:00",
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = onLocationClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(id = R.string.cd_location_icon)
                    )
                    Text(
                        text = weather.cityName,
                    )
                }

                Text(
                    text = stringResource(id = R.string.last_fetched_at, lastFetchedTime),
                    modifier = Modifier.align(Alignment.CenterEnd),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${weather.temp} ֯",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp)
                )
                Text(
                    text = stringResource(id = R.string.feels_like, weather.feelsLike),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FeatureColumn(
                    topText = stringResource(id = R.string.wind_top_text),
                    altText = "${weather.windSpeed} m/h"
                )
                FeatureColumn(
                    topText = stringResource(id = R.string.humidity_top_text),
                    altText = "${weather.humidity} %"
                )
                FeatureColumn(
                    topText = stringResource(id = R.string.visibility_top_text),
                    altText = "${weather.visibility} m"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stringResource(id = R.string.forecast),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    LineChart(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .height(216.dp),
                        graphStyle = GraphStyle(backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        yAxisData = forecast.temperaturesBy5Days.first(),
                        xAxisData = forecast.hoursBy5Days.first()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FeelsLikeCardPrev() {
    ComposeScaffoldProjectTheme {
        CurrentWeatherCard(
            weather = WeatherUiModel(
                id = 9540,
                description = "Rain",
                iconUrl = "https://www.google.com",
                cityName = "New York",
                feelsLike = "17",
                grndLevel = "augue",
                humidity = "2",
                pressure = "vocibus",
                seaLevel = "reque",
                temp = "7",
                tempMax = "10",
                tempMin = "0",
                visibility = "10000",
                windSpeed = "3.09"
            ),
            onLocationClick = {},
            lastFetchedTime = "12:00",
            forecast = ForecastUiModel(
                cod = null,
                city = null,
                temperaturesBy5Days = listOf(),
                hoursBy5Days = listOf()
            )
        )
    }
}