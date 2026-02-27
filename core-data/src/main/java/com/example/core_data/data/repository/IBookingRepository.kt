package com.example.core_data.data.repository

import com.example.core_data.data.model.ArenaWithCourts
import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.Courts
import com.example.core_data.data.remote.api.IBookingApi
import kotlinx.coroutines.flow.Flow

interface IBookingRepository : IBookingApi {
    fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts>
    fun getSubDomainCourts(subDomain: String): Flow<List<Courts>>
    fun getArenaById(id: String): Flow<Arenas?>
}