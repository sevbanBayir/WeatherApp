package com.sevban.data.mapper

import com.sevban.model.Forecast
import com.sevban.network.model.forecast.ForecastDTO

fun ForecastDTO.toForecast()= Forecast(
    cod = this.cod ?: ""
)