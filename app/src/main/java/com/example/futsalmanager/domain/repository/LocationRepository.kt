package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLiveLocation(): Flow<LocationModel>
    suspend fun checkGpsStatus(): Boolean
    suspend fun checkLocationStatus(): Boolean

    fun observeLocationStatus(): Flow<Boolean>
}

