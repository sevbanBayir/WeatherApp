package com.sevban.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherCard(
            weather = weather,
            onLocationClick = onLocationClick,
            forecast = forecast
        )

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
    }
}
