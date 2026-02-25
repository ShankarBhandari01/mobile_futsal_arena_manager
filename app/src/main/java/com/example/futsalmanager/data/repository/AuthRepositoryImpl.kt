package com.example.futsalmanager.data.repository

import com.example.futsalmanager.data.remote.api.AuthApi
import com.example.futsalmanager.data.remote.dto.ChangePasswordRequest
import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RefreshTokenResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse
import com.example.futsalmanager.data.remote.dto.ResetCodeResponse
import com.example.futsalmanager.domain.model.User
import com.example.futsalmanager.domain.repository.AuthRepository
import com.example.futsalmanager.domain.session.SessionStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        val response = api.login(email, password)
        response.onSuccess { sessionStorage.saveSession(it) }
        return response
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return api.register(registerRequest)
    }

    override val getUser: Flow<User?> get() = sessionStorage.userFlow

    override suspend fun logout(): Result<Unit> {
        return api.logout()
    }

    override suspend fun refresh(refreshToken: String): Result<RefreshTokenResponse> {
        return api.refresh(refreshToken)
    }

    override suspend fun forgotPassword(email: String): Result<ResetCodeResponse> {
        return api.forgotPassword(email)
    }

    override suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest): Result<Unit> {
        return api.resetPassword(changePasswordRequest)
    }

    override suspend fun verifyEmail(email: String, otp: String): Result<Unit> {
        return api.verifyEmail(email, otp)
    }

    override suspend fun getAccessToken(): String? = sessionStorage.getAccessToken()

    override suspend fun getRefreshToken() =
        sessionStorage.getRefreshToken()

    override suspend fun clear(isAutoLogout: Boolean) {
        if (isAutoLogout) {
            sessionStorage.clear()
        } else {
            this.logout()
        }

    }

}