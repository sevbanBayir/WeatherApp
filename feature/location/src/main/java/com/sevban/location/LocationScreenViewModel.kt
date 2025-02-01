package com.sevban.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.location.Geocoder
import com.sevban.common.location.LocationObserver
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.location.helper.PlacesAutocompleter
import com.sevban.location.model.LocationScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    locationObserver: LocationObserver,
    private val placesAutocompleter: PlacesAutocompleter,
    private val geocoder: Geocoder
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    fun onEvent(event: LocationScreenEvent) {
        when (event) {
            is LocationScreenEvent.OnSearchQueryChanged -> {
                _searchQuery.update { event.query }

                viewModelScope.launch {
                    placesAutocompleter.getAutocomplete(event.query)
                        .map { it.map { geocoder.getPlaceCoordinates(it) } }
                        .catch { _error.send(it) }
                        .collectLatest { places ->
                            _uiState.update { state ->
                                state.copy(autocompletePredictions = places.filterNotNull())
                            }
                        }
                }
            }

            is LocationScreenEvent.OnLocationSelected -> {
                _uiState.update { state ->
                    state.copy(
                        selectedPlace = event.prediction,
                        autocompletePredictions = emptyList()
                    )
                }
            }
        }
    }

}