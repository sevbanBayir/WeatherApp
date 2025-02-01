package com.sevban.location

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.sevban.location.model.LocationScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun LocationScreenRoute(
    viewModel: LocationScreenViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit
) {
    val locationUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val error = viewModel.error

    LocationScreen(
        uiState = locationUiState,
        searchQuery = searchQuery,
        error = error,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        onLocationClick = onLocationClick,
    )
}

@Composable
fun LocationScreen(
    uiState: LocationScreenUiState,
    searchQuery: String,
    onEvent: (LocationScreenEvent) -> Unit,
    onLocationClick: () -> Unit,
    error: Flow<Throwable>,
    whenErrorOccurred: suspend (Throwable, String?) -> Unit
) {
    val context = LocalContext.current
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val mapColorScheme = remember(isSystemInDarkTheme) {
        if (isSystemInDarkTheme) ComposeMapColorScheme.DARK
        else ComposeMapColorScheme.LIGHT
    }

    val properties = remember {
        MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        )
    }

    val mapCameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedPlace) {
        if (uiState.selectedPlace != null) {
            val cameraPosition = CameraPosition.builder()
                .target(LatLng(uiState.selectedPlace.latitude, uiState.selectedPlace.longitude))
                .zoom(7f)
                .build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            coroutineScope.launch { mapCameraPositionState.animate(cameraUpdate, 1500) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        GoogleMap(
            modifier = Modifier.align(Alignment.Center),
            mapColorScheme = mapColorScheme,
            properties = properties,
            cameraPositionState = mapCameraPositionState
        )
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onEvent(LocationScreenEvent.OnSearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            if (uiState.autocompletePredictions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(uiState.autocompletePredictions) { prediction ->
                        Card(
                            modifier = Modifier.animateItem(),
                            onClick = {
                                onEvent(LocationScreenEvent.OnLocationSelected(prediction))
                            }
                        ) {
                            Text(
                                text = prediction.name + ", " + prediction.country,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}