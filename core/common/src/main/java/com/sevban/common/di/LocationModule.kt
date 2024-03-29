package com.sevban.common.di

import android.content.Context
import com.sevban.common.location.LocationClient
import com.sevban.common.location.LocationClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context): com.sevban.common.location.LocationClient =
        com.sevban.common.location.LocationClientImpl(context)
}