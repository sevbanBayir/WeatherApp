package com.sevban.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.maps.android.compose.GoogleMap
import com.sevban.location.model.LocationScreenUiState
import kotlinx.coroutines.flow.Flow

@Composable
fun LocationScreenRoute(
    viewModel: LocationScreenViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error = viewModel.error

    LocationScreen(
        uiState = homeUiState,
        error = error,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        onLocationClick = onLocationClick,
    )
}

@Composable
fun LocationScreen(
    uiState: LocationScreenUiState,
    onEvent: (LocationScreenEvet) -> Unit,
    onLocationClick: () -> Unit,
    error: Flow<Throwable>,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    GoogleMap()
}