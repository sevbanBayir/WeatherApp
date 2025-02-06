package com.sevban.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.ui.model.ForecastWeatherUi

@Composable
fun ForecastRow(
    forecast: List<ForecastWeatherUi>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            forecast.forEachIndexed { index, it ->
                if (index == 0) {
                    ElevatedCard(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.widthIn(min = 120.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ForecastCard(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                forecastWeatherUi = it
                            )
                        }
                    }
                } else {
                    ForecastCard(
                        modifier = Modifier
                            .widthIn(min = 120.dp),
                        forecastWeatherUi = it
                    )
                }
            }
        }
    }
}