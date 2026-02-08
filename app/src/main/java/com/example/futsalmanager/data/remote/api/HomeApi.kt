package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.domain.model.LocationModel


interface HomeApi {
    suspend fun getArenaList(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse>
}
