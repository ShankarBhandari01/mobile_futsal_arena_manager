package com.example.futsalmanager.core.di

import com.example.futsalmanager.data.remote.api.IAuthApi
import com.example.futsalmanager.data.remote.api.IBookingApi
import com.example.futsalmanager.data.remote.api.IHomeApi
import com.example.futsalmanager.data.remote.api.IPaymentApi
import com.example.futsalmanager.data.remote.api.impl.IAuthApiImpl
import com.example.futsalmanager.data.remote.api.impl.IBookingApiImpl
import com.example.futsalmanager.data.remote.api.impl.IHomeApiImpl
import com.example.futsalmanager.data.remote.api.impl.IPaymentApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingsModule {

    @Binds
    @Singleton
    abstract fun bindAuthApi(impl: IAuthApiImpl): IAuthApi

    @Binds
    @Singleton
    abstract fun bindBookingApi(impl: IBookingApiImpl): IBookingApi

    @Binds
    @Singleton
    abstract fun bindHomeApi(impl: IHomeApiImpl): IHomeApi

    @Binds
    @Singleton
    abstract fun bindPaymentApi(impl: IPaymentApiImpl): IPaymentApi
}