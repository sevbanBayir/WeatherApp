package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.home.R
import com.sevban.home.model.WeatherUiModel

@Composable
fun WeatherFeaturesCard(
    weather: WeatherUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
}