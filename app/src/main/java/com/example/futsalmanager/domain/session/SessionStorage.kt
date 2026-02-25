package com.example.futsalmanager.domain.session

import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SessionStorage {

    suspend fun saveSession(response: LoginResponse)

    suspend fun getAccessToken(): String?
    suspend fun updateSessionTokens(accessToken: String, refreshToken: String, expiresIn: Int)

    suspend fun getRefreshToken(): String?

    suspend fun clear()

    val userFlow: Flow<User?>
}
