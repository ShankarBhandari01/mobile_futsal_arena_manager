package com.example.futsalmanager.data.remote.api.impl

import com.example.futsalmanager.data.remote.api.ApiRegistry
import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.data.remote.dto.ArenaListResponse
import com.example.futsalmanager.data.remote.safe.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class HomeApiImpl @Inject constructor(
    private val client: HttpClient
) : HomeApi {

    override suspend fun getArenaList(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse> {
        return safeApiCall {
            client.get(ApiRegistry.ARENA_LIST) {
                parameter("search", search)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("lat", lat)
                parameter("lng", lng)
            }.body<ArenaListResponse>()
        }
    }

}