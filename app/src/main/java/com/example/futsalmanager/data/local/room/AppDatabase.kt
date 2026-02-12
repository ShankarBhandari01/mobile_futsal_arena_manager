package com.example.futsalmanager.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.futsalmanager.data.local.room.dao.ArenaDao
import com.example.futsalmanager.data.local.room.typeConvertor.Converters
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts

@TypeConverters(Converters::class)
@Database(entities = [Arenas::class, Courts::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun arenaDao(): ArenaDao

}