package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.home.components.WeatherContent
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.ErrorScreen
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: () -> Unit
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = homeUiState,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        onLocationClick = onLocationClick,
        weatherState = weatherState,
        onFutureDaysForecastClick = onFutureDaysForecastClick
    )
}

@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: () -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current

    AnimatedContent(
        targetState = weatherState,
        modifier = Modifier.padding(16.dp),
        label = "WeatherAnimatedContent"
    ) {
        when (it) {
            is WeatherState.Error -> ErrorScreen(
                whenErrorOccurred = whenErrorOccurred,
                failure = it.failure,
                onTryAgainClick = { onEvent(HomeScreenEvent.OnTryAgainClick) }
            )

            is WeatherState.Loading -> LoadingScreen(1f)
            is WeatherState.Success -> WeatherContent(
                weather = it.weather,
                forecast = it.forecast,
                onLocationClick = onLocationClick,
                lastFetchedTime = uiState.lastFetchedTime,
                onFutureDaysForecastClick = onFutureDaysForecastClick
            )
        }
    }

    if (uiState.shouldShowPermanentlyDeclinedDialog)
        PermissionAlertDialog(
            onConfirmed = {
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
                context.openAppSettings()
            },
            onDismissed = {
                onEvent(HomeScreenEvent.OnPermissionDialogDismissed)
            }
        )

    PermissionRequester(
        onPermissionFirstDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionDeclined)
        },
        onPermissionPermanentlyDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
        }
    )
}