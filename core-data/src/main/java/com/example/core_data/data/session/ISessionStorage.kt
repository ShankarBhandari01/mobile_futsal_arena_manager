package com.example.core_data.data.session

import com.example.core_data.data.model.User
import com.example.core_data.data.remote.dto.LoginResponse
import kotlinx.coroutines.flow.Flow

interface ISessionStorage {

    suspend fun saveSession(response: LoginResponse)

    suspend fun getAccessToken(): String?
    suspend fun updateSessionTokens(accessToken: String, refreshToken: String, expiresIn: Int)

    suspend fun getRefreshToken(): String?

    suspend fun clear()

    val userFlow: Flow<User?>
}
