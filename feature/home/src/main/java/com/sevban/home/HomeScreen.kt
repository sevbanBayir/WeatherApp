package com.sevban.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.hasLocationPermission
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.extensions.shouldShowPermissionRationale
import com.sevban.model.Weather
import com.sevban.common.model.Failure
import com.sevban.ui.PermissionAlertDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onListItemClicked: (String) -> Unit,
    whenErrorOccured: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val error = viewModel.error

    LaunchedEffect(key1 = true) {
        viewModel.getWeatherWithLocation()
    }

    HomeScreen(
        weatherState,
        homeUiState = homeUiState,
        onListItemClicked = onListItemClicked,
        error = error,
        whenErrorOccured = whenErrorOccured,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HomeScreen(
    weather: Weather?,
    homeUiState: UiState,
    onListItemClicked: (String) -> Unit,
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Home Feature Screen")
        Button(onClick = { onListItemClicked("5") }) {
            Text(text = "Navigate to Detail")
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
        Text(text = weather.toString())
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
                    } else if (context.shouldShowPermissionRationale(permission).not() && context.hasLocationPermission().not()) {
                        onPermissionPermanentlyDeclined()
                    } else {
                        onPermissionFirstDeclined()
                    }
                }
            }
        )

    Button(onClick = {
        activityResultLauncher.launch(permissions)
    }) {
        Text(text = "Grant location permissions")
    }

}