package com.example.core_domain.domain.apis


import com.example.core_domain.domain.dto.CourtDto
import com.example.core_domain.domain.model.Arenas
import com.example.core_domain.domain.model.Slot


interface IBookingApi {
    suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>>

    suspend fun arenasSubDomainCourts(subDomain: String): Result<List<CourtDto>>
    suspend fun arenaSubDomain(subDomain: String): Result<Arenas>
}