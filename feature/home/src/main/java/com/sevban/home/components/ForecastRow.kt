package com.sevban.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.home.mapper.ForecastUiModel

@Composable
fun ForecastRow(
    forecast: ForecastUiModel,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            forecast.forecastBy3Hours.forEachIndexed { index, it ->
                if (index == 0) {
                    ElevatedCard(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        ForecastCard(
                            modifier = Modifier.padding(4.dp),
                            forecastWeatherUi = it
                        )
                    }
                } else {
                    ForecastCard(
                        modifier = Modifier.padding(8.dp),
                        forecastWeatherUi = it
                    )
                }
            }
        }
    }
}