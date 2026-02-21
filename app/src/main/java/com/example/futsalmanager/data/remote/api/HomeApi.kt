package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.ArenaListResponse


interface HomeApi {
    suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse>

}
