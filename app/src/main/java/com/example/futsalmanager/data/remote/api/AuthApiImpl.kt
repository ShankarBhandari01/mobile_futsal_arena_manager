package com.example.futsalmanager.data.remote.api

import com.example.futsalmanager.data.remote.dto.LoginRequest
import com.example.futsalmanager.data.remote.dto.LoginResponse
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse
import com.example.futsalmanager.data.remote.safe.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiImpl(
    private val client: HttpClient
) : AuthApi {

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
            client.post(ApiRegistry.LOGOUT)
        }
    }

    override suspend fun refresh(): Result<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return safeApiCall {
            client.post(ApiRegistry.FORGOT_PASSWORD)
        }
    }

    override suspend fun verifyEmail(email: String): Result<Unit> {
        return safeApiCall {
            client.post(ApiRegistry.VERIFY_EMAIL)
        }
    }
}
