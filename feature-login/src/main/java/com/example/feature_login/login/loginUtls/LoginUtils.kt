package com.example.feature_login.login.loginUtls

import com.example.core_domain.domain.dto.ChangePasswordRequest
import com.example.core_domain.domain.dto.RegisterRequest
import com.example.feature_login.login.AuthState
import com.example.feature_login.login.password_reset.OtpPasswordResetState

object LoginUtils {
    fun AuthState.toRegisterRequest(): com.example.core_domain.domain.dto.RegisterRequest {
        return _root_ide_package_.com.example.core_domain.domain.dto.RegisterRequest(
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

    fun OtpPasswordResetState.toChangePasswordRequest(): com.example.core_domain.domain.dto.ChangePasswordRequest {
        return _root_ide_package_.com.example.core_domain.domain.dto.ChangePasswordRequest(
            email = this.email,
            otp = this.otp,
            newPassword = this.password
        )
    }
}