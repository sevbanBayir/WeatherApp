package com.sevban.home

import android.Manifest
import android.app.Activity
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
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.extensions.shouldShowPermissionRationale
import com.sevban.model.Weather
import com.sevban.network.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onListItemClicked: (String) -> Unit,
    whenErrorOccured: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()

    val error = viewModel.error
    val shouldRedirectToSettings by
        viewModel.redirectToSettings.receiveAsFlow().collectAsStateWithLifecycle(
            initialValue = false
        )

    PermissionRequester(
        onPermissionPermanentlyDeclined = {
            viewModel.onEvent(HomeScreenEvent.OnLocationPermissionPermanentlyDeclined)
        },
        onPermissionResult = {
            viewModel.onEvent(HomeScreenEvent.OnLocationPermissionGranted)
        },
        shouldRedirectToSettings = shouldRedirectToSettings
    )
    HomeScreen(
        weatherState,
        homeUiState = homeUiState,
        onListItemClicked = onListItemClicked,
        error = error,
        whenErrorOccured = whenErrorOccured
    )
}

@Composable
fun HomeScreen(
    weather: Weather?,
    homeUiState: UiState,
    onListItemClicked: (String) -> Unit,
    error: Flow<Failure>,
    whenErrorOccured: suspend (Failure, String?) -> Unit
) {
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
        Text(text = weather.toString())
    }
}

@Composable
fun PermissionRequester(
    onPermissionResult: (Map<String, Boolean>) -> Unit,
    onPermissionPermanentlyDeclined: (String) -> Unit,
    shouldRedirectToSettings: Boolean
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val activityResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = {
                onPermissionResult(it)
                permissions.forEach { permission ->
                    if (context.shouldShowPermissionRationale(permission).not())
                        onPermissionPermanentlyDeclined(permission)
                }
            }
        )

    if (shouldRedirectToSettings)
        context.openAppSettings()

    Button(onClick = {
        activityResultLauncher.launch(permissions)
    }) {
        Text(text = "Grant location permissions")
    }

}