package com.example.core_data.data.remote.api


import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.Courts
import com.example.core_data.data.model.Slot
import com.example.core_data.data.remote.dto.CourtDto


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