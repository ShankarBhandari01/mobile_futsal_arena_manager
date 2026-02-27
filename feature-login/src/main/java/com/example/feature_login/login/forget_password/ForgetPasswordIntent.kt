package com.example.feature_login.login.forget_password


sealed interface ForgetPasswordIntent {
    data object OnBackClicked : ForgetPasswordIntent
    data class EmailChanged(val email: String) : ForgetPasswordIntent
    data object SubmitClicked : ForgetPasswordIntent
}
