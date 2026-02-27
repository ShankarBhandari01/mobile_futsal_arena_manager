package com.example.core_data.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.core_data.data.model.ArenaWithCourts
import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.Courts
import kotlinx.coroutines.flow.Flow

@Dao
interface IArenaDao {

    // Arena
    @Upsert
    suspend fun upsertArena(arena: com.example.core_data.data.model.Arenas)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArenas(arenas: List<com.example.core_data.data.model.Arenas>)

    @Query("SELECT * FROM Arenas WHERE id = :id")
    fun getArenaById(id: String): Flow<com.example.core_data.data.model.Arenas?>

    @Query("SELECT * FROM Arenas")
    fun getAllArenas(): Flow<List<Arenas>>

    @Query("DELETE FROM Arenas")
    suspend fun clearAllArenas()

    // Courts

    @Transaction
    suspend fun insertArenaAndCourts(arena: Arenas, courts: List<Courts>) {
        upsertArena(arena)
        upsertCourts(courts)
    }

    @Upsert
    suspend fun upsertCourts(courts: List<Courts>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourts(courts: List<com.example.core_data.data.model.Courts>)

    @Query("SELECT * FROM Courts WHERE id = :id")
    fun getCourtsById(id: String): Flow<com.example.core_data.data.model.Courts?>

    @Query("SELECT * FROM Courts")
    fun getAllCourts(): Flow<List<com.example.core_data.data.model.Courts>>

    @Query("DELETE FROM Courts")
    suspend fun clearAllCourts()

    // Arena + Courts relation
    @Transaction
    @Query("SELECT * FROM Arenas WHERE id = :id")
    fun getArenaWithCourts(id: String): Flow<com.example.core_data.data.model.ArenaWithCourts>
}
