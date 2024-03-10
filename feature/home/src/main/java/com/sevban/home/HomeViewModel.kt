package com.sevban.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.location.LocationClient
import com.sevban.model.Weather
import com.sevban.network.Failure
import com.sevban.ui.util.handleFailures
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationClient: LocationClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Failure>()
    val error = _error.receiveAsFlow()

    fun getLocation() {
        locationClient.getLastKnownLocation(onLocationUpdated = {
            viewModelScope.launch {
                getWeatherUseCase.execute(
                    it.latitude.toString(),
                    it.longitude.toString(),
                ).collect { weather ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            weather = weather
                        )
                    }
                }
            }
        })
    }
}