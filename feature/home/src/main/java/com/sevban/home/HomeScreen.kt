package com.sevban.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.common.extensions.openAppSettings
import com.sevban.common.model.Failure
import com.sevban.home.components.FeelsLikeCard
import com.sevban.home.components.HumidityCard
import com.sevban.home.components.forecastquadrant.LineChart
import com.sevban.home.components.forecastquadrant.generateTemperatureList
import com.sevban.model.Forecast
import com.sevban.ui.components.LoadingScreen
import com.sevban.ui.components.PermissionAlertDialog
import com.sevban.ui.components.PermissionRequester
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    whenErrorOccurred: suspend (Failure, String?) -> Unit,
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
        uiState = homeUiState,
        error = error,
        whenErrorOccurred = whenErrorOccurred,
        onEvent = viewModel::onEvent,
        forecast = forecastState
    )
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    forecast: Forecast?,
    uiState: WeatherScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    error: Flow<Failure>,
    whenErrorOccurred: suspend (Failure, String?) -> Unit
) {

    val context = LocalContext.current
    var tempList by remember { mutableStateOf(generateTemperatureList(20, 32, 8)) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        error.collectLatest {
            whenErrorOccurred(it, null)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedContent(
            targetState = uiState.isLoading,
            label = "WeatherScreenContent"
        ) { targetState ->

            when (targetState) {
                true -> LoadingScreen()
                false -> {
                    if (uiState.weather != null && forecast != null)
                        WeatherScreenContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            weather = uiState.weather,
                            temperatureList = tempList/*forecast.temp.map { it.temperature!!.toInt() }*/,
                            onClickButton = {
                                tempList = generateTemperatureList(0, 20, 8)
                                coroutineScope.launch {
                                    viewModel.getWeatherWithLocation()
                                }

                                coroutineScope.launch {
                                    viewModel.getForecastWithLocation()
                                }
                            })

                }
            }
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

}

@Composable
fun WeatherScreenContent(
    weather: WeatherUiModel,
    temperatureList: List<Int>,
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FeelsLikeCard(
            feelsLikeTemp = weather.feelsLike,
            currentTemp = weather.temp,
            weatherDescription = weather.description,
            weatherIconUrl = weather.iconUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        HumidityCard(
            wind = weather.windSpeed,
            humidity = weather.humidity,
            visibility = weather.visibility
        )

        LineChart(modifier = Modifier.padding(16.dp), yAxisData = temperatureList)

        Button(onClick = onClickButton) {
            Text(text = "Generate Temperature")
        }
    }
}