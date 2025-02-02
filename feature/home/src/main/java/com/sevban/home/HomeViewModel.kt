package com.sevban.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.location.LocationObserver
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.mapper.toForecastUiModel
import com.sevban.home.model.WeatherScreenUiState
import com.sevban.home.model.WeatherState
import com.sevban.home.model.toWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    locationObserver: LocationObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    val weatherState = combine(
        locationObserver.getCurrentLocation(),
        savedStateHandle.getStateFlow<Double?>(LATITUDE_ARG, null),
        savedStateHandle.getStateFlow<Double?>(LONGITUDE_ARG, null)
    ) { location, lat, long ->
        if (lat != null && long != null) {
            lat to long
        } else {
            location.latitude to location.longitude
        }
    }
        .retry { cause ->
            if (cause is MissingLocationPermissionException) {
                delay(3.seconds)
                true
            } else {
                _error.send(cause)
                false
            }
        }
        .flatMapLatest { (latitude, longitude) ->
            combine(
                getWeatherUseCase.execute(
                    lat = latitude.toString(),
                    long = longitude.toString()
                ),
                getForecastUseCase.execute(
                    lat = latitude.toString(),
                    long = longitude.toString()
                )
            ) { weather, forecast ->
                WeatherState.Success(
                    weather = weather.toWeatherUiModel(),
                    forecast = forecast.toForecastUiModel()
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeatherState.Loading
        )

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnLocationPermissionDeclined -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = true
                    )
                }
            }

            is HomeScreenEvent.OnLocationPermissionPermanentlyDeclined -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = true
                    )
                }
            }

            is HomeScreenEvent.OnPermissionDialogDismissed -> {
                _uiState.update {
                    it.copy(
                        shouldShowPermanentlyDeclinedDialog = false
                    )
                }
            }
        }
    }

    companion object {
        private const val LATITUDE_ARG = "latitude"
        private const val LONGITUDE_ARG = "longitude"
    }
}