package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.repository.AuthRepositoryImpl
import com.example.futsalmanager.domain.repository.AuthRepository
import com.example.futsalmanager.domain.session.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        sessionStorage: SessionStorage,
        api: AuthApi
    ): AuthRepository = AuthRepositoryImpl(api, sessionStorage)
}