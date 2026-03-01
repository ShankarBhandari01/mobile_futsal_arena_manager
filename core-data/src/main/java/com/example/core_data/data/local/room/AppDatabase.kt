package com.example.core_data.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core_data.data.local.room.dao.IArenaDao
import com.example.core_data.data.local.room.typeConvertor.Converters
import com.example.core_domain.domain.model.Arenas
import com.example.core_domain.domain.model.Courts

@TypeConverters(Converters::class)
@Database(entities = [Arenas::class, Courts::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun arenaDao(): IArenaDao

}