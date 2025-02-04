package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.model.toLocalizedMessage
import com.sevban.home.components.WeatherContent
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = homeUiState,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        onLocationClick = onLocationClick,
        weatherState = weatherState,
    )
}

@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    onLocationClick: () -> Unit,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current

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

    PermissionRequester(
        onPermissionFirstDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionDeclined)
        },
        onPermissionPermanentlyDeclined = {
            onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        AnimatedContent(
            targetState = weatherState,
            label = "WeatherAnimatedContent"
        ) { weatherState ->
            when (weatherState) {
                is WeatherState.Error -> LaunchedEffect(key1 = uiState) {
                    whenErrorOccurred(
                        weatherState.failure,
                        weatherState.failure.errorType.toLocalizedMessage(context)
                    )
                }

                is WeatherState.Loading -> LoadingScreen()
                is WeatherState.Success -> WeatherContent(
                    weather = weatherState.weather,
                    forecast = weatherState.forecast,
                    onLocationClick = onLocationClick,
                    onFutureDaysForecastClick = {
                        // TODO: Implement future days forecast click
                    }
                )
            }
        }
    }
}