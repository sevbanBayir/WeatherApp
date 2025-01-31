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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.home.components.WeatherContent
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val error = viewModel.error

    HomeScreen(
        uiState = homeUiState,
        error = error,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        weatherState = weatherState,
    )
}

@Composable
fun HomeScreen(
    weatherState: WeatherState,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    error: Flow<Throwable>,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = true) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            error.collectLatest {
                whenErrorOccurred(it, null)
            }
        }
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
                is WeatherState.Loading -> LoadingScreen()
                is WeatherState.Success -> WeatherContent(
                    weather = weatherState.weather,
                    forecast = weatherState.forecast,
                    onLocationClick = {
                        // TODO: Implement location click
                    },
                    onFutureDaysForecastClick = {
                        // TODO: Implement future days forecast click
                    }
                )
            }
        }
    }
}