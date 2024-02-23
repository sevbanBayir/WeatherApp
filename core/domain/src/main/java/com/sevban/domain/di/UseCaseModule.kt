package com.sevban.domain.di

import com.sevban.data.repository.Repository
import com.sevban.domain.usecase.GetCharacterListUseCase
import com.sevban.domain.usecase.GetCharacterUseCase
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
    fun provideGetCharacterUseCase(repository: Repository) = GetCharacterUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCharacterListUseCase(repository: Repository) = GetCharacterListUseCase(repository)
}