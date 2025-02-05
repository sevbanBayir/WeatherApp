package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.mapper.ChartData
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
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth()
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
                hoursBy5Days = listOf(),
                forecastBy3Hours = listOf(),
                chartData = ChartData(
                    temperatures = listOf(),
                    dateList = listOf()
                )
            )
        )
    }
}