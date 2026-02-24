package com.example.futsalmanager.data.di

import android.content.Context
import com.example.futsalmanager.core.utils.LogoutEventBus
import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.api.BookingApi
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.api.PaymentApi
import com.example.futsalmanager.data.remote.api.impl.AuthApiImpl
import com.example.futsalmanager.data.remote.api.impl.BookingApiImpl
import com.example.futsalmanager.data.remote.api.impl.HomeApiImpl
import com.example.futsalmanager.data.remote.api.impl.PaymentApiImpl
import com.example.futsalmanager.data.remote.client.HttpClientFactory
import com.example.futsalmanager.domain.session.SessionStorage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {
        var authApi: AuthApi? = null

        @Provides
        @Singleton
        fun provideHttpClient(sessionStorage: SessionStorage): HttpClient {
            return HttpClientFactory.create(
                tokenProvider = {
                    runBlocking {
                        sessionStorage.getAccessToken()
                    }
                },
                sessionStorage = sessionStorage,
                getAuthApi = {
                    requireNotNull(authApi) { "AuthApi not initialized before token refresh" }
                },
                onLogout = { LogoutEventBus.triggerLogout() }
            ).also {
                authApi = AuthApiImpl(it)
            }
        }

        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }
    }

    @Binds
    @Singleton
    abstract fun bindAuthApi(impl: AuthApiImpl): AuthApi

    @Binds
    @Singleton
    abstract fun bindHomeApi(impl: HomeApiImpl): HomeApi

    @Binds
    @Singleton
    abstract fun bindBookingApi(impl: BookingApiImpl): BookingApi

    @Binds
    @Singleton
    abstract fun bindPaymentApi(impl: PaymentApiImpl): PaymentApi
}
