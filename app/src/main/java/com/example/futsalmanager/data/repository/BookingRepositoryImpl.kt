package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.local.room.dao.ArenaDao
import com.example.futsalmanager.data.remote.api.BookingApi
import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.domain.model.ArenaWithCourts
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val arenaDao: ArenaDao,
    private val api: BookingApi
) : BookingRepository {

    override fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts?> {
        return arenaDao.getArenaWithCourts(arenaId)
    }

    override fun getSubDomainCourts(subDomain: String): Flow<List<Courts>> {
        return arenaDao.getAllCourts()
    }

    override fun getArenaById(id: String): Flow<Arenas?> {
        return arenaDao.getArenaById(id)
    }


    override suspend fun getCourtSlots(
        subDomain: String,
        courtId: String,
        date: String,
        includeStatus: Boolean
    ): Result<List<Slot>> {
        return api.getCourtSlots(subDomain, courtId, date, includeStatus)
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

    override suspend fun arenaSubDomain(subDomain: String): Result<Arenas> {
        // call api
        val response = api.arenaSubDomain(subDomain)

        response.onSuccess { arena ->
            // insert into database
            arenaDao.upsertArena(arena)
        }
        return response
    }


}