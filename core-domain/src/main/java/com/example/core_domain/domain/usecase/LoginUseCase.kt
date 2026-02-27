package com.example.core_domain.domain.usecase


import com.example.core_data.data.remote.dto.ChangePasswordRequest
import com.example.core_data.data.remote.dto.RegisterRequest
import com.example.core_data.data.remote.dto.RegisterResponse
import com.example.core_data.data.repository.IAuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val repo: IAuthRepository
) {
    // write all the use cases here
    suspend operator fun invoke(email: String, password: String) = repo.login(email, password)

    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return repo.register(registerRequest)
    }

    suspend fun logout(isAutoLogout: Boolean = false) = repo.clear(isAutoLogout)
    suspend fun getAccessToken() = repo.getAccessToken()
    suspend fun getRefreshToken() = repo.getRefreshToken()
    val userFlow get() = repo.getUser
    suspend fun forgotPassword(email: String) = repo.forgotPassword(email)
    suspend fun verifyEmail(email: String, code: String) = repo.verifyEmail(email, code)
    suspend fun refresh() = repo.refresh(getRefreshToken().orEmpty())
    suspend fun resetPassword(changePasswordRequest: ChangePasswordRequest) =
        repo.resetPassword(changePasswordRequest)

}