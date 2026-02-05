package com.example.futsalmanager.data.di

import com.example.futsalmanager.data.local.session.SessionDataStore
import com.example.futsalmanager.domain.session.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindSessionStorage(
        sessionDataStore: SessionDataStore
    ): SessionStorage

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}