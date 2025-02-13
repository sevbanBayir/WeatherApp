package com.sevban.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.sevban.detail.components.AllDaysForecastContent
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen

@Composable
fun DetailScreen(
    onEvent: (DetailScreenEvent) -> Unit,
    forecastState: ForecastState,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (forecastState) {
            is ForecastState.Loading -> LoadingScreen(Modifier.testTag("loading_screen"), 1f)
            is ForecastState.Success -> AllDaysForecastContent(
                modifier = Modifier.testTag("success_screen"),
                forecast = forecastState.forecast
            )

            is ForecastState.Error -> ErrorScreen(
                modifier = Modifier.testTag("error_screen"),
                whenErrorOccurred = whenErrorOccurred,
                failure = forecastState.failure,
                onTryAgainClick = { onEvent(DetailScreenEvent.OnTryAgainClick) }
            )
        }
    }
}

