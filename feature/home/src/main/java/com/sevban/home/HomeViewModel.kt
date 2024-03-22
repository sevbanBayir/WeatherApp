package com.sevban.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.constants.Constants.istanbulLatitude
import com.sevban.common.location.LocationClient
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.common.model.Failure
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.model.Forecast
import com.sevban.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationClient: LocationClient,
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Failure>()
    val error = _error.receiveAsFlow()

    private val _weatherState = MutableStateFlow<Weather?>(null)
    val weatherState: StateFlow<Weather?> = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<Forecast?>(null)
    val forecastState: StateFlow<Forecast?> = _forecastState.asStateFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnLocationPermissionGranted -> {
                viewModelScope.launch {
                    launch {
                        getWeatherWithLocation()
                    }
                    launch {
                        getForecastWithLocation()
                    }
                }
            }

            is HomeScreenEvent.OnLocationPermissionDeclined -> Unit

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

    suspend fun getWeatherWithLocation() {
        locationClient.getLastKnownLocation()
            .catch {
                if (it is MissingLocationPermissionException)
                    emit(null)
                else
                    throw it
            }
            .collect { location ->
                _weatherState.update {
                    getWeatherUseCase.execute(
                        location?.latitude?.toString() ?: istanbulLatitude,
                        location?.longitude?.toString() ?: istanbulLatitude
                    ).first()
                }
            }
    }

    suspend fun getForecastWithLocation() {
        locationClient.getLastKnownLocation()
            .catch {
                if (it is MissingLocationPermissionException)
                    emit(null)
                else
                    throw it
            }
            .collect { location ->
                _forecastState.update {
                    getForecastUseCase.execute(
                        location?.latitude?.toString() ?: istanbulLatitude,
                        location?.longitude?.toString() ?: istanbulLatitude
                    ).first()
                }
            }
    }
}