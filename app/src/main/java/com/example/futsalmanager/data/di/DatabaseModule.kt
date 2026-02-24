package com.example.futsalmanager.data.di

import android.content.Context
import androidx.room.Room
import com.example.futsalmanager.data.local.room.AppDatabase
import com.example.futsalmanager.data.local.room.dao.ArenaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "futsal_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideArenaDao(db: AppDatabase): ArenaDao {
        return db.arenaDao()
    }
}
