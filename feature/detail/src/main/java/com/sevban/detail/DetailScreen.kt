package com.sevban.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.detail.components.AllDaysForecastContent
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen

@Composable
fun DetailScreenRoute(
    viewModel: DetailViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    val weatherState by viewModel.forecastState.collectAsStateWithLifecycle()

    DetailScreen(
        forecastState = weatherState,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun DetailScreen(
    onEvent: (DetailScreenEvent) -> Unit,
    forecastState: ForecastState,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    AnimatedContent(
        targetState = forecastState,
        modifier = Modifier.padding(16.dp),
        label = "WeatherAnimatedContent"
    ) {
        when (it) {
            is ForecastState.Error -> ErrorScreen(
                whenErrorOccurred = whenErrorOccurred,
                failure = it.failure,
                onTryAgainClick = { onEvent(DetailScreenEvent.OnTryAgainClick) }
            )

            is ForecastState.Loading -> LoadingScreen(1f)
            is ForecastState.Success -> AllDaysForecastContent(
                forecast = it.forecast,
            )
        }
    }
}

