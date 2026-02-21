package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.HomeApi
import com.example.futsalmanager.domain.model.Arenas
import kotlinx.coroutines.flow.Flow

interface HomeRepository : HomeApi {
    fun getArenaListFromDB(): Flow<List<Arenas>>
}