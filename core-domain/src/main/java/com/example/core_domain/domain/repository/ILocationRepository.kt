package com.example.core_domain.domain.repository

import com.example.core_domain.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getLiveLocation(): Flow<LocationModel>
    suspend fun checkGpsStatus(): Boolean
    suspend fun checkLocationStatus(): Boolean

    fun observeLocationStatus(): Flow<Boolean>
}

