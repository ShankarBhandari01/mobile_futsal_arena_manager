package com.example.core_domain.domain.apis

import com.example.core_domain.domain.dto.ArenaListResponse




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
