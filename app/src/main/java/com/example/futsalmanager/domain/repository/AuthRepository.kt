package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository : AuthApi {
    // data store apis
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
    val userFlow: Flow<User?>
}
