package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.IBookingApi
import com.example.futsalmanager.domain.model.ArenaWithCourts
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import kotlinx.coroutines.flow.Flow

interface IBookingRepository : IBookingApi {
    fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts?>
    fun getSubDomainCourts(subDomain: String): Flow<List<Courts>>
    fun getArenaById(id: String): Flow<Arenas?>
}