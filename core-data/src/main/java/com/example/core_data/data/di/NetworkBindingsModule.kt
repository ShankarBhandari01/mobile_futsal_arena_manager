package com.example.core_data.data.di

import com.example.core_domain.domain.apis.IAuthApi
import com.example.core_domain.domain.apis.IBookingApi
import com.example.core_domain.domain.apis.IHomeApi
import com.example.core_domain.domain.apis.IPaymentApi
import com.example.core_data.data.remote.api.impl.IAuthApiImpl
import com.example.core_data.data.remote.api.impl.IBookingApiImpl
import com.example.core_data.data.remote.api.impl.IHomeApiImpl
import com.example.core_data.data.remote.api.impl.IPaymentApiImpl
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