package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.IHomeApi
import com.example.futsalmanager.domain.model.Arenas
import kotlinx.coroutines.flow.Flow

interface IHomeRepository : IHomeApi {
    fun getArenaListFromDB(): Flow<List<Arenas>>
}