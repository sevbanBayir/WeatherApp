package com.sevban.location.model

import com.sevban.model.Place

data class LocationScreenUiState(
    val selectedPlace: Place? = null,
    val autocompletePredictions: List<Place> = emptyList()
)
