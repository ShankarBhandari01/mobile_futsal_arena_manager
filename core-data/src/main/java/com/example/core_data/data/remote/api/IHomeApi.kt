package com.example.core_data.data.remote.api

import com.example.core_data.data.remote.dto.ArenaListResponse



interface IHomeApi {
    suspend fun getArenaListFromApi(
        search: String,
        offset: Int,
        limit: Int,
        date: String,
        lat: Double?,
        lng: Double?
    ): Result<ArenaListResponse>

}
