package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.domain.repository.AuthRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = repo.login(email, password)

    suspend fun register(registerRequest: RegisterRequest) = repo.register(registerRequest)
    suspend fun logout() = repo.clear()
    suspend fun getAccessToken() = repo.getAccessToken()
    suspend fun getRefreshToken() = repo.getRefreshToken()
    val userFlow get() = repo.userFlow

}