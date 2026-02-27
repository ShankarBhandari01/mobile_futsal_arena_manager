package com.example.core_data.data.remote.api.impl

import com.example.core_data.data.remote.api.ApiRegistry
import com.example.core_data.data.remote.api.IAuthApi
import com.example.core_data.data.remote.client.plugins.TokenRefreshPlugin
import com.example.core_data.data.remote.dto.ChangePasswordRequest
import com.example.core_data.data.remote.dto.LoginRequest
import com.example.core_data.data.remote.dto.LoginResponse
import com.example.core_data.data.remote.dto.RefreshTokenResponse
import com.example.core_data.data.remote.dto.RegisterRequest
import com.example.core_data.data.remote.dto.RegisterResponse
import com.example.core_data.data.remote.dto.ResetCodeResponse
import com.example.core_data.data.remote.safe.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class IAuthApiImpl @Inject constructor(
    private val client: HttpClient
) : IAuthApi {

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse> {
        return safeApiCall {
            client.post(ApiRegistry.LOGIN) {
                setBody(LoginRequest(email, password))
            }.body<LoginResponse>()
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return safeApiCall {
            client.post(ApiRegistry.REGISTER) {
                setBody(registerRequest)
            }.body<RegisterResponse>()
        }
    }

    override suspend fun logout(): Result<Unit> {
        return safeApiCall {
            client.post(ApiRegistry.LOGOUT) {
                attributes.put(TokenRefreshPlugin.IS_LOGOUT_REQUEST, true)
            }
        }
    }

    override suspend fun refresh(refreshToken: String): Result<RefreshTokenResponse> {
        return safeApiCall {
            client.post(ApiRegistry.REFRESH) {
                val body = buildJsonObject {
                    put("refreshToken", refreshToken)
                }

                setBody(body)
                attributes.put(TokenRefreshPlugin.IS_REFRESH_REQUEST, true)
            }.body<RefreshTokenResponse>()
        }
    }

    override suspend fun forgotPassword(email: String): Result<ResetCodeResponse> {
        return safeApiCall {
            client.post(ApiRegistry.FORGOT_PASSWORD) {
                val body = buildJsonObject {
                    put("email", email)
                }
                setBody(body)
            }.body<ResetCodeResponse>()
        }
    }

    override suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest): Result<Unit> {
        return safeApiCall {
            client.post(ApiRegistry.RESET_PASSWORD) {
                setBody(changePasswordRequest)
            }.body<Unit>()
        }
    }

    override suspend fun verifyEmail(email: String, otp: String): Result<Unit> {
        return safeApiCall {
            client.post(ApiRegistry.VERIFY_EMAIL) {
                val body = buildJsonObject {
                    put("email", email)
                    put("otp", otp)
                }
                setBody(body)
            }.body<Unit>()
        }
    }
}