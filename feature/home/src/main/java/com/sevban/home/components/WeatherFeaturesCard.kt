package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.WeatherAppIcons
import com.sevban.home.R
import com.sevban.home.model.WeatherUiModel

@Composable
fun WeatherFeaturesCard(
    weather: WeatherUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FeatureRow(
            icon = {
                Icon(
                    imageVector = WeatherAppIcons.Wind,
                    modifier = Modifier.size(30.dp),
                    contentDescription = stringResource(id = R.string.cd_wind_icon),
                )
            },
            text = stringResource(id = R.string.wind, weather.windSpeed)
        )
        FeatureRow(
            icon = {
                Icon(
                    imageVector = WeatherAppIcons.Humidity,
                    modifier = Modifier.size(20.dp),
                    contentDescription = stringResource(id = R.string.cd_humidity_icon),
                )
            },
            text = stringResource(id = R.string.humidity, weather.humidity)
        )
        FeatureRow(
            icon = {
                Icon(
                    imageVector = WeatherAppIcons.Visibility,
                    modifier = Modifier.size(20.dp),
                    contentDescription = stringResource(id = R.string.cd_visibility_icon),
                )
            },
            text = stringResource(id = R.string.visibility, weather.visibility)
        )
    }
}