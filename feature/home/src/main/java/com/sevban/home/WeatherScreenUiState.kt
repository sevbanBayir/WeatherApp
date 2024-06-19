package com.sevban.home

import com.sevban.model.Forecast
import com.sevban.model.Weather

data class WeatherScreenUiState (
    val weather: WeatherUiModel? = null,
    val forecast: Forecast? = null,
    val isLoading: Boolean = false,
    val shouldShowPermanentlyDeclinedDialog: Boolean = false
)

/*sealed interface WeatherScreenUiState {
    data object Loading: WeatherScreenUiState
    data class Success(
        val weather: Weather? = null,
        val forecast: Forecast? = null,
    )
}
*/
