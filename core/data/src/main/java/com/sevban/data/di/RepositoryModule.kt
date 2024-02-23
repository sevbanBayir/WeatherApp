package com.sevban.data.di

import com.sevban.data.repository.Repository
import com.sevban.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repositoryImpl: RepositoryImpl): Repository
}