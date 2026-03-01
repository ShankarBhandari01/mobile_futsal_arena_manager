package com.example.core_data.data.di

import android.content.Context
import com.example.core_domain.domain.apis.IAuthApi
import com.example.core_data.data.remote.client.HttpClientFactory
import com.example.core_domain.domain.session.ISessionStorage
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
                // LogoutEventBus.triggerLogout()
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


