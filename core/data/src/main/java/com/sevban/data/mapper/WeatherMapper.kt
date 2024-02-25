package com.sevban.data.mapper

import com.sevban.model.Character
import com.sevban.model.Weather
import com.sevban.network.model.CharacterDTO
import com.sevban.network.model.weather.WeatherDTO

fun WeatherDTO.toWeather() = Weather(
    id = id ?: 0
)