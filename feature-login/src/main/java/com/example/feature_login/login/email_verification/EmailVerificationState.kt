package com.example.feature_login.login.email_verification

data class EmailVerificationState(
    val email: String = "",
    val code: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
