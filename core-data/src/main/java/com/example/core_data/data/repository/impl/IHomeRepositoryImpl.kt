package com.example.core_data.data.repository.impl

import com.example.core_data.data.local.room.dao.IArenaDao
import com.example.core_data.data.model.Arenas
import com.example.core_data.data.remote.api.IHomeApi
import com.example.core_data.data.remote.dto.ArenaListResponse
import com.example.core_data.data.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IHomeRepositoryImpl @Inject constructor(
    private val IArenaDao: IArenaDao,
    private val api: IHomeApi
) : IHomeRepository {

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
                IArenaDao.clearAllArenas()
            }
            IArenaDao.insertArenas(networkArenas)
        }
        return response
    }


    override fun getArenaListFromDB(): Flow<List<Arenas>> {
        return IArenaDao.getAllArenas()
    }


}