package com.example.futsalmanager.domain.repository

import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse
import com.example.futsalmanager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun clear()

    val userFlow: Flow<User?>
}
