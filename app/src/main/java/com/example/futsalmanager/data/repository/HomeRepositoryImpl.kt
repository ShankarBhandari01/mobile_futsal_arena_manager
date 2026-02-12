package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.local.room.dao.ArenaDao
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.domain.model.ArenaWithCourts
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val arenaDao: ArenaDao,
    private val api: HomeApi
) : HomeRepository {

    override suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse> {
        val response = api.getArenaListFromApi(
            search,
            offset,
            limit,
            date,
            lat,
            lng
        )
        response.onSuccess {
            val networkArenas = response.getOrNull()?.arenas ?: emptyList()
            if (offset == 0) {
                arenaDao.clearAllArenas()
            }
            arenaDao.insertArenas(networkArenas)
        }
        return response
    }

    override suspend fun arenaSubDomain(subDomain: String): Result<Arenas> {
        val response = api.arenaSubDomain(subDomain)

        response.onSuccess { arena ->
            arenaDao.upsertArena(arena)
        }
        return response
    }

    override suspend fun arenasSubDomainCourts(subDomain: String): Result<List<CourtDto>> {
        val arenaResult = api.arenaSubDomain(subDomain)

        arenaResult.onSuccess { arena ->
            val courtsResult = api.arenasSubDomainCourts(subDomain)

            courtsResult.onSuccess { courts ->
                val courtsWithArenaId = courts.map { dto ->
                    Courts(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        capacity = dto.capacity,
                        basePrice = dto.basePrice,
                        slotDurationMinutes = dto.slotDurationMinutes,
                        type = dto.type,
                        amenities = dto.amenities,
                        arenaId = arena.id
                    )
                }
                arenaDao.insertArenaAndCourts(arena, courtsWithArenaId)
            }.onFailure {
                return courtsResult
            }
        }
        return arenaResult.map { listOf() }
    }

    override suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>> {
        return api.getCourtSlots(subDomain, courtId, date, includeStatus)
    }

    override fun getArenaListFromDB(): Flow<List<Arenas>> {
        return arenaDao.getAllArenas()
    }

    override fun getArenaById(id: String): Flow<Arenas?> {
        return arenaDao.getArenaById(id)
    }

    override fun getSubDomainCourts(subDomain: String): Flow<List<Courts>> {
        return arenaDao.getAllCourts()
    }

    override fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts?> {
        return arenaDao.getArenaWithCourts(arenaId)
    }

}