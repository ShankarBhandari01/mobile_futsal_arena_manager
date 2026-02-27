package com.example.feature_login.login.loginUtls

import com.example.core_data.data.remote.dto.ChangePasswordRequest
import com.example.core_data.data.remote.dto.RegisterRequest
import com.example.feature_login.login.AuthState
import com.example.feature_login.login.password_reset.OtpPasswordResetState

object LoginUtils {
    fun AuthState.toRegisterRequest(): RegisterRequest {
        return RegisterRequest(
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            password = this.password,
            phone = this.phone
        )
    }

    fun String.isValidPassword(): Boolean {
        return this.length >= 8
    }

    fun OtpPasswordResetState.toChangePasswordRequest(): ChangePasswordRequest {
        return ChangePasswordRequest(
            email = this.email,
            otp = this.otp,
            newPassword = this.password
        )
    }
}