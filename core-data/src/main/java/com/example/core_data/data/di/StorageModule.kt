package com.example.core_data.data.di

import com.example.core_data.data.local.session.ISessionDataStore
import com.example.core_data.data.session.ISessionStorage
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
        sessionDataStore: ISessionDataStore
    ): ISessionStorage

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}