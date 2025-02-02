package com.sevban.common.di

import com.sevban.common.helper.DispatcherProvider
import com.sevban.common.helper.StandardDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    abstract fun provideDispatcherProvider(impl: StandardDispatcherProvider): DispatcherProvider
}