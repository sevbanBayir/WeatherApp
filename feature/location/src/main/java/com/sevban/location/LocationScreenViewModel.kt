package com.sevban.location

import androidx.lifecycle.ViewModel
import com.sevban.common.location.LocationObserver
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.location.model.LocationScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LocationScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    locationObserver: LocationObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    fun onEvent(event: LocationScreenEvet) {

    }
}