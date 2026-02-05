package com.example.futsalmanager.ui.login.password_reset

sealed interface OtpPasswordResetEffect {
    object OnBackClicked : OtpPasswordResetEffect
    data class ShowError(val message: String) : OtpPasswordResetEffect
    object Navigate : OtpPasswordResetEffect
}
