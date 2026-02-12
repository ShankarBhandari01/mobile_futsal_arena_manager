package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.domain.model.ArenaWithCourts
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import kotlinx.coroutines.flow.Flow

interface HomeRepository : HomeApi {
    fun getArenaListFromDB(): Flow<List<Arenas>>

    fun getArenaById(id: String): Flow<Arenas?>

    fun getSubDomainCourts(subDomain: String): Flow<List<Courts>>
    fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts?>
}