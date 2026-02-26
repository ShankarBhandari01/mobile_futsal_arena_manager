package com.example.futsalmanager.core.di

import com.example.futsalmanager.data.location.ILocationServiceImpl
import com.example.futsalmanager.data.repository.IAuthRepositoryImpl
import com.example.futsalmanager.data.repository.IBookingRepositoryImpl
import com.example.futsalmanager.data.repository.IHomeRepositoryImpl
import com.example.futsalmanager.data.repository.IPaymentRepositoryImpl
import com.example.futsalmanager.domain.repository.IAuthRepository
import com.example.futsalmanager.domain.repository.IBookingRepository
import com.example.futsalmanager.domain.repository.IHomeRepository
import com.example.futsalmanager.domain.repository.ILocationRepository
import com.example.futsalmanager.domain.repository.IPaymentRepository
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
