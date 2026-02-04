package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse

interface AuthApi {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun logout(): Result<Unit>
    suspend fun refresh(): Result<LoginResponse>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun verifyEmail(email: String): Result<Unit>
}
