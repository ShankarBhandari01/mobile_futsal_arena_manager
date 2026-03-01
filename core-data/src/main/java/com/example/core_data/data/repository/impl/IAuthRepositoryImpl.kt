package com.example.core_data.data.repository.impl

import com.example.core_domain.domain.model.User
import com.example.core_domain.domain.apis.IAuthApi
import com.example.core_domain.domain.repository.IAuthRepository
import com.example.core_domain.domain.session.ISessionStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IAuthRepositoryImpl @Inject constructor(
    private val api: IAuthApi,
    private val ISessionStorage: ISessionStorage
) : IAuthRepository {

    override suspend fun login(email: String, password: String): Result<com.example.core_domain.domain.dto.LoginResponse> {
        val response = api.login(email, password)
        response.onSuccess { ISessionStorage.saveSession(it) }
        return response
    }

    override suspend fun register(registerRequest: com.example.core_domain.domain.dto.RegisterRequest): Result<com.example.core_domain.domain.dto.RegisterResponse> {
        return api.register(registerRequest)
    }

    override val getUser: Flow<User?> get() = ISessionStorage.userFlow

    override suspend fun logout(): Result<Unit> {
        return api.logout()
    }

    override suspend fun refresh(refreshToken: String): Result<com.example.core_domain.domain.dto.RefreshTokenResponse> {
        return api.refresh(refreshToken)
    }

    override suspend fun forgotPassword(email: String): Result<com.example.core_domain.domain.dto.ResetCodeResponse> {
        return api.forgotPassword(email)
    }

    override suspend fun resetPassword(changePasswordRequest: com.example.core_domain.domain.dto.ChangePasswordRequest): Result<Unit> {
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