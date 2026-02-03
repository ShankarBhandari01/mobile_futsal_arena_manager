package com.example.futsalmanager.data.di

import android.content.Context
import com.example.futsalmanager.data.local.session.SessionDataStore
import com.example.futsalmanager.domain.session.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideSessionStorage(
        @ApplicationContext context: Context
    ): SessionStorage {
        return SessionDataStore(context, Json)
    }
}
