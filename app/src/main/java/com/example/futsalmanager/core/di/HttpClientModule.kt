package com.example.futsalmanager.core.di

import android.content.Context
import com.example.futsalmanager.core.ui.states.LogoutEventBus
import com.example.futsalmanager.data.remote.api.IAuthApi
import com.example.futsalmanager.data.remote.client.HttpClientFactory
import com.example.futsalmanager.domain.session.ISessionStorage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        ISessionStorage: ISessionStorage,
        IAuthApiProvider: Provider<IAuthApi>
    ): HttpClient {

        return HttpClientFactory.create(
            tokenProvider = { ISessionStorage.getAccessToken() },
            ISessionStorage = ISessionStorage,
            getIAuthApi = { IAuthApiProvider.get() },
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


