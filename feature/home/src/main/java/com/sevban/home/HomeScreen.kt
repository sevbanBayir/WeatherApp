package com.sevban.home

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.model.Weather
import com.sevban.network.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onListItemClicked: (String) -> Unit,     //-> hoist navigation actions to appState's navController.
    whenErrorOccured: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error = viewModel.error
    PermissionRequester {
        viewModel.getLocation()
    }
    HomeScreen(
        homeUiState = homeUiState,
        onListItemClicked = onListItemClicked,
        error = error,
        whenErrorOccured = whenErrorOccured
    )
}

@Composable
fun HomeScreen(
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
        Text(text = homeUiState.weather.toString())
    }
}

@Composable
fun PermissionRequester(
    onPermissionResult: (Map<String, Boolean>) -> Unit
) {

    val activityResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = {
                onPermissionResult(it)
            }
        )

    Button(onClick = {
        activityResultLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }) {
        Text(text = "Grant location permissions")
    }
}