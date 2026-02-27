package com.example.feature_login.login.password_reset

sealed interface OtpPasswordResetEffect {
    object OnBackClicked : OtpPasswordResetEffect
    data class ShowError(val message: String) : OtpPasswordResetEffect
    object Navigate : OtpPasswordResetEffect
}
