package com.sevban.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.constants.Constants.istanbulLatitude
import com.sevban.common.location.LocationClient
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.network.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    locationClient: LocationClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Failure>()
    val error = _error.receiveAsFlow()

    private val retryChannel = Channel<Boolean>()

    val redirectToSettings = Channel<Boolean>()

    val weatherState = locationClient.getLastKnownLocation()
        .retryWhen { cause, attempt ->
            cause is MissingLocationPermissionException && retryChannel.receive() && attempt < 2
        }
        .catch {
            if (it is MissingLocationPermissionException)
                emit(null)
        }
        .map { location ->
            getWeatherUseCase.execute(
                location?.latitude?.toString() ?: istanbulLatitude,
                location?.longitude?.toString() ?: istanbulLatitude
            ).first()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnLocationPermissionGranted -> {
                viewModelScope.launch {
                    retryChannel.send(true)
                }
            }

            is HomeScreenEvent.OnLocationPermissionPermanentlyDeclined -> {
                viewModelScope.launch {
                    redirectToSettings.send(true)
                }
            }
        }
    }

}