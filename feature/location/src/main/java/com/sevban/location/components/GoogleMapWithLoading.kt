package com.sevban.location.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.sevban.model.Place
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.model.WeatherUiModel

@Composable
fun GoogleMapWithLoading(
    mapColorScheme: ComposeMapColorScheme,
    properties: MapProperties,
    onMapLoaded: () -> Unit,
    mapCameraPositionState: CameraPositionState,
    markerLocation: Place?,
    weatherForSelectedLocation: WeatherUiModel?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    GoogleMap(
        modifier = modifier,
        mapColorScheme = mapColorScheme,
        properties = properties,
        onMapLoaded = onMapLoaded,
        cameraPositionState = mapCameraPositionState,
    ) {
        if (markerLocation != null && weatherForSelectedLocation != null)
            PlaceWeatherMarker(
                markerLocation = markerLocation,
                weatherForSelectedLocation = weatherForSelectedLocation
            )
    }
    AnimatedVisibility(
        visible = isLoading,
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        LoadingScreen(
            heightFraction = 1f,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    }
}