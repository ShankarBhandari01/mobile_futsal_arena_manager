package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.repository.HomeRepository
import com.example.futsalmanager.domain.session.SessionStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val api: HomeApi
) : HomeRepository {

    override suspend fun getArenaList(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse> {
        return api.getArenaList(
            search,
            offset,
            limit,
            date,
            lat,
            lng
        )
    }

}