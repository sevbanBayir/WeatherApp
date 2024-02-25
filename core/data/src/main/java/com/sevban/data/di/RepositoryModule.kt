package com.sevban.data.di

import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.data.repository.WeatherForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repositoryImpl: WeatherForecastRepositoryImpl): WeatherForecastRepository
}