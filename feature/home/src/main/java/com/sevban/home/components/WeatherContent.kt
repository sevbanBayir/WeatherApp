package com.sevban.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sevban.home.R
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.home.model.WeatherUiModel

@Composable
fun WeatherContent(
    weather: WeatherUiModel,
    forecast: ForecastUiModel,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherCard(
            weather = weather,
            onLocationClick = onLocationClick,
            forecast = forecast
        )

        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.today_all_data_title),
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                TextButton(
                    onClick = onFutureDaysForecastClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.future_days_forecast_button),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            ElevatedCard {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    forecast.forecastBy3Hours.forEach {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Spacer(Modifier.height(4.dp))
                            Text(text = it.date)
                            AsyncImage(
                                model = it.icon,
                                contentDescription = it.description
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(text = it.description)
                            Text(text = "${it.temperature} ֯")
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}
