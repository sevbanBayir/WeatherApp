package com.sevban.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.sevban.common.extensions.hasLocationPermission
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.extensions.shouldShowPermissionRationale
import com.sevban.common.model.Failure
import com.sevban.home.components.FeelsLikeCard
import com.sevban.home.components.HumidityCard
import com.sevban.model.Forecast
import com.sevban.ui.PermissionAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccured: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()
    val error = viewModel.error

    LaunchedEffect(key1 = true) {
        viewModel.getWeatherWithLocation()
        viewModel.getForecastWithLocation()
    }

    HomeScreen(
        weather = weatherState,
        homeUiState = homeUiState,
        error = error,
        whenErrorOccured = whenErrorOccured,
        onEvent = viewModel::onEvent,
        forecast = forecastState
    )
}

@Composable
fun HomeScreen(
    weather: WeatherUiModel?,
    forecast: Forecast?,
    homeUiState: UiState,
    onEvent: (HomeScreenEvent) -> Unit,
    error: Flow<Failure>,
    whenErrorOccured: suspend (Failure, String?) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        error.collectLatest {
            whenErrorOccured(it, null)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        weather?.let {
            FeelsLikeCard(
                feelsLikeTemp = weather.feelsLike,
                currentTemp = weather.temp,
                weatherDescription = weather.description,
                weatherIcon = R.drawable.broken_clouds
            )

            Spacer(modifier = Modifier.height(16.dp))

            HumidityCard(
                wind = weather.windSpeed,
                humidity = weather.humidity,
                visibility = weather.visibility
            )
        }

        PermissionRequester(
            onPermissionGranted = {
                onEvent(HomeScreenEvent.OnLocationPermissionGranted)
            },
            onPermissionFirstDeclined = {

            },
            onPermissionPermanentlyDeclined = {
                onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
            },

            )
//        Text(text = weather.toString())
//        Text(text = forecast.toString())
    }

    if (homeUiState.shouldShowPermanentlyDeclinedDialog)
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

@Composable
fun PermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionFirstDeclined: () -> Unit,
    onPermissionPermanentlyDeclined: () -> Unit,
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissionResults ->
                permissions.forEach { permission ->
                    if (permissionResults[permission] == true) {
                        onPermissionGranted()
                    } else if (context.shouldShowPermissionRationale(permission)
                            .not() && context.hasLocationPermission().not()
                    ) {
                        onPermissionPermanentlyDeclined()
                    } else {
                        onPermissionFirstDeclined()
                    }
                }
            }
        )

    LaunchedEffect(key1 = true) {
        activityResultLauncher.launch(permissions)
    }
    /*    Button(onClick = {
            activityResultLauncher.launch(permissions)
        }) {
            Text(text = "Grant location permissions")
        }*/

}