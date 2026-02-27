package com.example.feature_login.login.password_reset

data class OtpPasswordResetState(
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
