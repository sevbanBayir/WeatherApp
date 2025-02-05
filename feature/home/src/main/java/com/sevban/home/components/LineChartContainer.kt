package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sevban.home.R
import com.sevban.home.components.forecastquadrant.GraphStyle
import com.sevban.home.components.forecastquadrant.LineChart
import com.sevban.home.mapper.ForecastUiModel

@Composable
fun LineChartContainer(
    forecast: ForecastUiModel,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
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
                yAxisData = forecast.chartData.temperatures,
                xAxisData = forecast.chartData.dateList
            )
        }
    }
}