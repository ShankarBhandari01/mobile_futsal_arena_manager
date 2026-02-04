package com.example.futsalmanager.ui.login.email_verification

data class EmailVerificationState(
    val email: String = "",
    val code: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
