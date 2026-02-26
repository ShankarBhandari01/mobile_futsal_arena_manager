package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.mapper.CourtsMapper
import com.example.futsalmanager.data.local.room.dao.IArenaDao
import com.example.futsalmanager.data.remote.api.IBookingApi
import com.example.futsalmanager.data.remote.dto.CourtDto
import com.example.futsalmanager.domain.model.ArenaWithCourts
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Slot
import com.example.futsalmanager.domain.repository.IBookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IBookingRepositoryImpl @Inject constructor(
    private val IArenaDao: IArenaDao,
    private val api: IBookingApi
) : IBookingRepository {

    override fun getArenaWithCourts(arenaId: String): Flow<ArenaWithCourts?> {
        return IArenaDao.getArenaWithCourts(arenaId)
    }

    override fun getSubDomainCourts(subDomain: String): Flow<List<Courts>> {
        return IArenaDao.getAllCourts()
    }

    override fun getArenaById(id: String): Flow<Arenas?> {
        return IArenaDao.getArenaById(id)
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

            courtsResult.onSuccess { dtos ->
                val courtsEntities = dtos.map { dto ->
                    CourtsMapper().toDomain(dto, arena.id)
                }
                IArenaDao.insertArenaAndCourts(arena, courtsEntities)
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
            IArenaDao.upsertArena(arena)
        }
        return response
    }


}