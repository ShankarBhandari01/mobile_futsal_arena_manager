package com.example.core_domain.domain.session

import com.example.core_domain.domain.dto.LoginResponse
import com.example.core_domain.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ISessionStorage {

    suspend fun saveSession(response: LoginResponse)

    suspend fun getAccessToken(): String?
    suspend fun updateSessionTokens(accessToken: String, refreshToken: String, expiresIn: Int)

    suspend fun getRefreshToken(): String?

    suspend fun clear()

    val userFlow: Flow<User?>
}
