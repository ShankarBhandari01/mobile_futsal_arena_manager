package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.ChangePasswordRequest
import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RefreshTokenResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse
import com.example.futsalmanager.data.remote.dto.ResetCodeResponse

interface IAuthApi {
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun verifyEmail(email: String,otp:String): Result<Unit>
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun refresh(refreshToken: String): Result<RefreshTokenResponse>
    suspend fun logout(): Result<Unit>
    suspend fun forgotPassword(email: String): Result<ResetCodeResponse>
    suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest): Result<Unit>
}
