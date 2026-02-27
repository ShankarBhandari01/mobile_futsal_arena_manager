package com.example.core_data.data.repository.impl

import com.example.core_data.data.model.User
import com.example.core_data.data.remote.api.IAuthApi
import com.example.core_data.data.remote.dto.ChangePasswordRequest
import com.example.core_data.data.remote.dto.LoginResponse
import com.example.core_data.data.remote.dto.RefreshTokenResponse
import com.example.core_data.data.remote.dto.RegisterRequest
import com.example.core_data.data.remote.dto.RegisterResponse
import com.example.core_data.data.remote.dto.ResetCodeResponse
import com.example.core_data.data.repository.IAuthRepository
import com.example.core_data.data.session.ISessionStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IAuthRepositoryImpl @Inject constructor(
    private val api: IAuthApi,
    private val ISessionStorage: ISessionStorage
) : IAuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        val response = api.login(email, password)
        response.onSuccess { ISessionStorage.saveSession(it) }
        return response
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return api.register(registerRequest)
    }

    override val getUser: Flow<User?> get() = ISessionStorage.userFlow

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

    override suspend fun getAccessToken(): String? = ISessionStorage.getAccessToken()

    override suspend fun getRefreshToken() =
        ISessionStorage.getRefreshToken()

    override suspend fun clear(isAutoLogout: Boolean) {
        if (isAutoLogout) {
            ISessionStorage.clear()
        } else {
            ISessionStorage.clear()
            this.logout()
        }
    }

}