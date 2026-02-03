package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse

interface AuthApi {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(RegisterRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun logout(): Result<Unit>
}
