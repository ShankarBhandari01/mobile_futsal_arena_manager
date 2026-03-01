package com.example.core_domain.domain.repository

import com.example.core_domain.domain.apis.IBookingApi
import com.example.core_domain.domain.model.ArenaWithCourts
import com.example.core_domain.domain.model.Arenas
import com.example.core_domain.domain.model.Courts
import kotlinx.coroutines.flow.Flow

interface IBookingRepository : IBookingApi {
    fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts>
    fun getSubDomainCourts(subDomain: String): Flow<List<Courts>>
    fun getArenaById(id: String): Flow<Arenas?>
}