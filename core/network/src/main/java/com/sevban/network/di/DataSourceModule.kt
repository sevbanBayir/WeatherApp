package com.sevban.network.di

import com.sevban.network.source.remote.RemoteDataSource
import com.sevban.network.source.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl) : RemoteDataSource
}