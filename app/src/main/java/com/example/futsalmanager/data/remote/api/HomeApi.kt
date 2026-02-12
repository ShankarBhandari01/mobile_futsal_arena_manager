package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Slot


interface HomeApi {
    suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse>

    suspend fun arenaSubDomain(subDomain: String): Result<Arenas>
    suspend fun arenasSubDomainCourts(subDomain: String): Result<List<CourtDto>>

    suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>>
}
