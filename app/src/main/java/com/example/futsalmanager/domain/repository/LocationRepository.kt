package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLiveLocation(): Flow<LocationModel>
    fun checkGpsStatus(): Boolean
    fun checkLocationStatus(): Boolean
}

