package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.api.AuthApiImpl
import com.example.futsalmanager.data.remote.client.HttpClientFactory
import com.example.futsalmanager.data.repository.AuthRepositoryImpl
import com.example.futsalmanager.domain.repository.AuthRepository
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        sessionStorage: SessionStorage
    ): HttpClient {
        return HttpClientFactory.create {
            runBlocking { sessionStorage.getAccessToken() }
        }
    }

    @Provides
    fun provideAuthApi(client: HttpClient): AuthApi = AuthApiImpl(client)

}
