package com.example.core_data.data.di

import com.example.core_data.data.location.ILocationServiceImpl
import com.example.core_data.data.repository.IBookingRepository
import com.example.core_data.data.repository.impl.IAuthRepositoryImpl
import com.example.core_data.data.repository.impl.IBookingRepositoryImpl
import com.example.core_data.data.repository.impl.IHomeRepositoryImpl
import com.example.core_data.data.repository.impl.IPaymentRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: IAuthRepositoryImpl
    ): com.example.core_data.data.repository.IAuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: IHomeRepositoryImpl
    ): com.example.core_data.data.repository.IHomeRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationServiceImpl: ILocationServiceImpl
    ): com.example.core_data.data.repository.ILocationRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: IBookingRepositoryImpl
    ): IBookingRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: IPaymentRepositoryImpl
    ): com.example.core_data.data.repository.IPaymentRepository
}
