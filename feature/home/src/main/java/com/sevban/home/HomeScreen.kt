package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.model.Failure
import com.sevban.home.components.ForecastContent
import com.sevban.home.components.WeatherContent
import com.sevban.home.components.forecastquadrant.generateTemperatureList
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()
    val error = viewModel.error

    HomeScreen(
        uiState = homeUiState,
        error = error,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        weatherState = weatherState,
        forecastState = forecastState
    )
}

@Composable
fun HomeScreen(
    weatherState: WeatherState,
    forecastState: ForecastState,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    error: Flow<Failure>,
    whenErrorOccurred: suspend (Failure, String?) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        error.collectLatest {
            whenErrorOccurred(it, null)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        AnimatedContent(
            targetState = weatherState,
            label = "WeatherAnimatedContent"
        ) { weatherState ->
            when (weatherState) {
                is WeatherState.Error -> {}
                is WeatherState.Loading -> LoadingScreen()
                is WeatherState.Success -> WeatherContent(weather = weatherState.weather)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = forecastState,
            label = "ForecastAnimatedContent"
        ) { forecastState ->
            when (forecastState) {
                is ForecastState.Error -> {}
                is ForecastState.Loading -> LoadingScreen()
                is ForecastState.Success -> ForecastContent(forecast = forecastState.forecast)
            }
        }

        PermissionRequester(
            onPermissionFirstDeclined = {
                onEvent(HomeScreenEvent.OnLocationPermissionDeclined)
            },
            onPermissionPermanentlyDeclined = {
                onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
            }
        )
    }

    if (uiState.shouldShowPermanentlyDeclinedDialog)
        PermissionAlertDialog(
            onConfirmed = {
                context.openAppSettings()
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
            },
            onDismissed = {
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
            }
        )

}