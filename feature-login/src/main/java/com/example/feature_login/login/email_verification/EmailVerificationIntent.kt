package com.example.feature_login.login.email_verification

sealed interface EmailVerificationIntent {
    data object OnBackClicked : EmailVerificationIntent
    data class EmailChanged(val email: String) : EmailVerificationIntent
    data class CodeChanged(val code: String) : EmailVerificationIntent
    data object SubmitClicked : EmailVerificationIntent
}