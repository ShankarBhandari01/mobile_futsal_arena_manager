package com.example.core_domain.domain.apis

import com.example.core_domain.domain.dto.ChangePasswordRequest
import com.example.core_domain.domain.dto.LoginResponse
import com.example.core_domain.domain.dto.RefreshTokenResponse
import com.example.core_domain.domain.dto.RegisterRequest
import com.example.core_domain.domain.dto.RegisterResponse
import com.example.core_domain.domain.dto.ResetCodeResponse


interface IAuthApi {
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun verifyEmail(email: String, otp: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun refresh(refreshToken: String): Result<RefreshTokenResponse>
    suspend fun logout(): Result<Unit>
    suspend fun forgotPassword(email: String): Result<ResetCodeResponse>
    suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest): Result<Unit>
}
