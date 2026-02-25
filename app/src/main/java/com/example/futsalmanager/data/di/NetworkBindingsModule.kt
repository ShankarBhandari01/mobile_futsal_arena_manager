package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.api.BookingApi
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.api.PaymentApi
import com.example.futsalmanager.data.remote.api.impl.AuthApiImpl
import com.example.futsalmanager.data.remote.api.impl.BookingApiImpl
import com.example.futsalmanager.data.remote.api.impl.HomeApiImpl
import com.example.futsalmanager.data.remote.api.impl.PaymentApiImpl
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
    abstract fun bindAuthApi(impl: AuthApiImpl): AuthApi

    @Binds
    @Singleton
    abstract fun bindBookingApi(impl: BookingApiImpl): BookingApi

    @Binds
    @Singleton
    abstract fun bindHomeApi(impl: HomeApiImpl): HomeApi

    @Binds
    @Singleton
    abstract fun bindPaymentApi(impl: PaymentApiImpl): PaymentApi
}