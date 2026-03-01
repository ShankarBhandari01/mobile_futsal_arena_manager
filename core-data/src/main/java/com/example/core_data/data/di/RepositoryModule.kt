package com.example.core_data.data.di

import com.example.core_data.data.location.ILocationServiceImpl
import com.example.core_domain.domain.repository.IBookingRepository
import com.example.core_data.data.repository.impl.IAuthRepositoryImpl
import com.example.core_data.data.repository.impl.IBookingRepositoryImpl
import com.example.core_data.data.repository.impl.IHomeRepositoryImpl
import com.example.core_data.data.repository.impl.IPaymentRepositoryImpl
import com.example.core_domain.domain.repository.IAuthRepository
import com.example.core_domain.domain.repository.IHomeRepository
import com.example.core_domain.domain.repository.ILocationRepository
import com.example.core_domain.domain.repository.IPaymentRepository

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
    ): IAuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: IHomeRepositoryImpl
    ): IHomeRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationServiceImpl: ILocationServiceImpl
    ): ILocationRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: IBookingRepositoryImpl
    ): IBookingRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: IPaymentRepositoryImpl
    ): IPaymentRepository
}
