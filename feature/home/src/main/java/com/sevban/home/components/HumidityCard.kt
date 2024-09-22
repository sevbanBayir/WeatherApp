package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.home.R

@Composable
fun HumidityCard(
    wind: String,
    humidity: String,
    visibility: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FeatureColumn(
                topText = stringResource(id = R.string.wind_top_text),
                altText = "$wind m/h"
            )
            FeatureColumn(
                topText = stringResource(id = R.string.humidity_top_text),
                altText = "$humidity %"
            )
            FeatureColumn(
                topText = stringResource(id = R.string.visibility_top_text),
                altText = "$visibility m"
            )
        }
    }
}

@Composable
fun FeatureColumn(
    topText: String,
    altText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = topText,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = altText,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HumidityLikeCardPrev() {
//    HumidityCard(wind = 14.15, humidity = 16.17, visibility = 18.19, modifier = Modifier)
}