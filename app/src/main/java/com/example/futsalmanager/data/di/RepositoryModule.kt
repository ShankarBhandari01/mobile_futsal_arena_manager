package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.location.LocationServiceImpl
import com.example.futsalmanager.data.repository.AuthRepositoryImpl
import com.example.futsalmanager.data.repository.BookingRepositoryImpl
import com.example.futsalmanager.data.repository.HomeRepositoryImpl
import com.example.futsalmanager.data.repository.PaymentRepositoryImpl
import com.example.futsalmanager.domain.repository.AuthRepository
import com.example.futsalmanager.domain.repository.BookingRepository
import com.example.futsalmanager.domain.repository.HomeRepository
import com.example.futsalmanager.domain.repository.LocationRepository
import com.example.futsalmanager.domain.repository.PaymentRepository
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
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationServiceImpl: LocationServiceImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
}
