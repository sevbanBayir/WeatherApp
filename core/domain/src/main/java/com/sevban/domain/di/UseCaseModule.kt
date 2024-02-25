package com.sevban.domain.di

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)// ??????????????????????????????????????
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetCharacterUseCase(repository: WeatherForecastRepository) =
        GetWeatherUseCase(repository)

}