package com.example.core_data.data.repository

import com.example.core_data.data.model.Arenas
import com.example.core_data.data.remote.api.IHomeApi
import kotlinx.coroutines.flow.Flow

interface IHomeRepository : IHomeApi {
    fun getArenaListFromDB(): Flow<List<Arenas>>
}