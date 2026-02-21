package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Slot

interface BookingApi {
    suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>>

    suspend fun arenasSubDomainCourts(subDomain: String): Result<List<CourtDto>>
    suspend fun arenaSubDomain(subDomain: String): Result<Arenas>
}