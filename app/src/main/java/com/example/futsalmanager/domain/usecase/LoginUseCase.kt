package com.example.futsalmanager.domain.usecase

import com.example.futsalmanager.data.remote.dto.ChangePasswordRequest
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.data.remote.dto.RegisterResponse
import com.example.futsalmanager.domain.repository.AuthRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    // write all the use cases here
    suspend operator fun invoke(email: String, password: String) = repo.login(email, password)


    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return repo.register(registerRequest)
    }

    suspend fun logout() = repo.clear()
    suspend fun getAccessToken() = repo.getAccessToken()
    suspend fun getRefreshToken() = repo.getRefreshToken()
    val userFlow get() = repo.userFlow

    suspend fun forgotPassword(email: String) = repo.forgotPassword(email)
    suspend fun verifyEmail(email: String, code: String) = repo.verifyEmail(email, code)
    suspend fun refresh() = repo.refresh()
    suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest) =
        repo.resetPassword(changePasswordRequest)

}