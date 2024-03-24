package com.sevban.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.home.R

@Composable
fun FeelsLikeCard(
    feelsLikeTemp: String,
    currentTemp: String,
    weatherDescription: String,
    @DrawableRes
    weatherIcon: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = stringResource(id = R.string.cd_weather_summary_card)
                )
                Text(
                    text = weatherDescription,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = "$currentTemp֯",
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(id = R.string.feels_like, feelsLikeTemp),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FeelsLikeCardPrev() {
/*    FeelsLikeCard(
        feelsLikeTemp = 4.5,
        currentTemp = 6.7,
        weatherIcon = R.drawable.ic_downloading,
        weatherDescription = "Heavy Rain"
    )*/
}