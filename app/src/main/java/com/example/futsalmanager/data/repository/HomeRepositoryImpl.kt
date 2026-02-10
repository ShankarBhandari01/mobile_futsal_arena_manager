package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.local.room.dao.ArenaDao
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.model.Arenas
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
                arenaDao.clearAll()
            }
            arenaDao.insertArenas(networkArenas)
        }
        return response
    }

    override fun getArenaListFromDB(): Flow<List<Arenas>> {
        return arenaDao.getAllArenas()
    }

    override fun getArenaById(id: String): Flow<Arenas?> {
      return  arenaDao.getArenaById(id)

    }

}