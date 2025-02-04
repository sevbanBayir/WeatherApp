package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.model.Failure
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
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        AnimatedContent(
            targetState = weatherState,
            label = "WeatherAnimatedContent"
        ) { weatherState ->
            when (weatherState) {
                is WeatherState.Error -> ErrorScreen(
                    whenErrorOccurred = whenErrorOccurred,
                    failure = weatherState.failure,
                    onTryAgainClick = { onEvent(HomeScreenEvent.OnTryAgainClick) }
                )

                is WeatherState.Loading -> LoadingScreen(1f)
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

@Composable
fun ErrorScreen(
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    failure: Failure,
    onTryAgainClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        whenErrorOccurred(
            failure,
            failure.errorType.toLocalizedMessage(context)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = failure.errorType.toLocalizedMessage(context),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onTryAgainClick) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}
