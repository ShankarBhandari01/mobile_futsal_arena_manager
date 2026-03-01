package com.example.core_data.data.remote.api.impl

import com.example.core_data.data.remote.api.ApiRegistry
import com.example.core_data.data.remote.safe.safeApiCall
import com.example.core_domain.domain.apis.IHomeApi
import com.example.core_domain.domain.dto.ArenaListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class IHomeApiImpl @Inject constructor(
    private val client: HttpClient
) : IHomeApi {

    override suspend fun getArenaListFromApi(
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