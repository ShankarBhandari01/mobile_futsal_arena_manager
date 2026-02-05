package com.example.futsalmanager.ui.login.password_reset

sealed interface OtpPasswordResetIntent {
    data class OnOtpChanged(val otp: String) : OtpPasswordResetIntent
    data class PasswordChanged(val password: String) : OtpPasswordResetIntent
    data class ConfirmPasswordChanged(val confirmPassword: String) : OtpPasswordResetIntent
    object SubmitClicked : OtpPasswordResetIntent
    object OnBackClicked : OtpPasswordResetIntent
}
