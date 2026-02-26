package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.api.IAuthApi
import com.example.futsalmanager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository : IAuthApi {
    // data store apis
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear(isAutoLogout: Boolean = false)
    val getUser: Flow<User?>
}
