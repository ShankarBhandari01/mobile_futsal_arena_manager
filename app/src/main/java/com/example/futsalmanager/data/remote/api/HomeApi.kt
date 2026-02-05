package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.ArenaListResponse


interface HomeApi {
    suspend fun getArenaList(
        search: String,
        offset: Int,
        limit: Int,
        date: String
    ): Result<ArenaListResponse>
}
