package com.example.futsalmanager.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.futsalmanager.domain.model.Arenas
import kotlinx.coroutines.flow.Flow

@Dao
interface ArenaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArenas(arenas: List<Arenas>)

    @Query("SELECT * FROM arenas")
    fun getAllArenas(): Flow<List<Arenas>>

    @Query("DELETE FROM arenas")
    suspend fun clearAll()

    @Query("SELECT * FROM arenas WHERE id = :id")
    fun getArenaById(id: String): Flow<Arenas?>
}