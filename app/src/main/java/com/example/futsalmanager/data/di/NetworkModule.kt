package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.api.impl.AuthApiImpl
import com.example.futsalmanager.data.remote.api.impl.HomeApiImpl
import com.example.futsalmanager.data.remote.client.HttpClientFactory
import com.example.futsalmanager.domain.session.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(sessionStorage: SessionStorage): HttpClient {
            return HttpClientFactory.create {
                runBlocking { sessionStorage.getAccessToken() }
            }
        }
    }

    @Binds
    @Singleton
    abstract fun bindAuthApi(impl: AuthApiImpl): AuthApi

    @Binds
    @Singleton
    abstract fun bindHomeApi(impl: HomeApiImpl): HomeApi

}
