package com.sevban.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.common.location.Geocoder
import com.sevban.common.location.LocationObserver
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.location.helper.PlaceAutocompleteService
import com.sevban.location.model.LocationScreenUiState
import com.sevban.ui.model.toWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LocationScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val placeAutocompleteService: PlaceAutocompleteService,
    private val geocoder: Geocoder,
    locationObserver: LocationObserver,
) : ViewModel() {

    private val _error = Channel<Throwable>()
    val error = _error.receiveAsFlow()

    private val _uiState = MutableStateFlow(LocationScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val placeList = _searchQuery
        .flatMapLatest { placeAutocompleteService.getAutocomplete(it) }
        .map { it.mapNotNull { searchQuery -> geocoder.getCityCoordinates(searchQuery) } }
        .catch { _error.send(it) }
        .flowOn(Dispatchers.Default) // TODO: inject dispatcher
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onEvent(event: LocationScreenEvent) {
        when (event) {
            is LocationScreenEvent.OnSearchQueryChanged -> {
                _searchQuery.update { event.query }
            }

            is LocationScreenEvent.OnLocationSelected -> {
                _uiState.update { it.copy(selectedPlace = event.prediction) }
                _searchQuery.update { "" }
                viewModelScope.launch {
                    getWeatherUseCase.execute(event.prediction.latitude.toString(),
                        event.prediction.longitude.toString()
                    )
                        .catch { _error.send(it) }
                        .collect { weather ->
                            _uiState.update { it.copy(weather = weather.toWeatherUiModel()) }
                        }

                }
            }
        }
    }

}