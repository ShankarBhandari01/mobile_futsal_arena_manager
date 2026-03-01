package com.example.core_domain.domain.repository

import com.example.core_domain.domain.apis.IHomeApi
import com.example.core_domain.domain.model.Arenas
import kotlinx.coroutines.flow.Flow

interface IHomeRepository : IHomeApi {
    fun getArenaListFromDB(): Flow<List<Arenas>>
}