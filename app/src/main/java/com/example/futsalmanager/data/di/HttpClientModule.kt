package com.example.futsalmanager.data.di

import android.content.Context
import com.example.futsalmanager.core.utils.LogoutEventBus
import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.client.HttpClientFactory
import com.example.futsalmanager.domain.session.SessionStorage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        sessionStorage: SessionStorage,
        authApiProvider: javax.inject.Provider<AuthApi>
    ): HttpClient {

        return HttpClientFactory.create(
            tokenProvider = { sessionStorage.getAccessToken() },
            sessionStorage = sessionStorage,
            getAuthApi = { authApiProvider.get() },
            onLogout = {
                LogoutEventBus.triggerLogout()
            }
        )
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}


