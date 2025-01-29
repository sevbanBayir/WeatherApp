package com.sevban.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.location.LocationObserver
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.common.model.Failure
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.mapper.ForecastUiModel
import com.sevban.home.mapper.toForecastUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Failure>()
    val error = _error.receiveAsFlow()

    val weatherState = locationObserver.getCurrentLocation()
        .retry { cause ->
            if (cause is MissingLocationPermissionException) {
                delay(3.seconds)
                true
            } else {
                false
            }
        }
        .flatMapLatest { location ->
            getWeatherUseCase.execute(
                lat = location.latitude.toString(),
                long = location.longitude.toString()
            )
        }.map {
            WeatherState.Success(it.toWeatherUiModel())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeatherState.Loading
        )

    val forecastState: StateFlow<ForecastState> =
        locationObserver.getCurrentLocation()
            .retryWhen { cause, attempt ->
                if (cause is MissingLocationPermissionException) {
                    delay(3.seconds)
                    true
                } else {
                    false
                }
            }
            .flatMapLatest { location ->
                getForecastUseCase.execute(
                    lat = location.latitude.toString(),
                    long = location.longitude.toString()
                )
            }
            .map {
                ForecastState.Success(it.toForecastUiModel())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(50000),
                initialValue = ForecastState.Loading
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
}

sealed interface WeatherState {
    data object Loading : WeatherState
    data class Success(val weather: WeatherUiModel) : WeatherState
    data class Error(val failure: Failure) : WeatherState
}

sealed interface ForecastState {
    data object Loading : ForecastState
    data class Success(val forecast: ForecastUiModel) : ForecastState
    data class Error(val failure: Failure) : ForecastState
}
