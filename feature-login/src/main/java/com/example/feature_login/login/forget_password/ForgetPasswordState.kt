package com.example.feature_login.login.forget_password

data class ForgetPasswordState(
    val email: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
