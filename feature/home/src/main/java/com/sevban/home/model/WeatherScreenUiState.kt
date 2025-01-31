package com.sevban.home.model

import com.sevban.common.model.Failure
import com.sevban.home.mapper.ForecastUiModel

data class WeatherScreenUiState(
    val shouldShowPermanentlyDeclinedDialog: Boolean = false
)

sealed interface WeatherState {
    data object Loading : WeatherState
    data class Success(val weather: WeatherUiModel, val forecast: ForecastUiModel) : WeatherState
    data class Error(val failure: Failure) : WeatherState
}