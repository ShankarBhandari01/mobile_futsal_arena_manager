package com.example.core_data.data.repository

import com.example.core_data.data.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getLiveLocation(): Flow<LocationModel>
    suspend fun checkGpsStatus(): Boolean
    suspend fun checkLocationStatus(): Boolean

    fun observeLocationStatus(): Flow<Boolean>
}

