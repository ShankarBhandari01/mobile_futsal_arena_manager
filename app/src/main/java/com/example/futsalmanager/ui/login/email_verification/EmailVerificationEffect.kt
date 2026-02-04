package com.example.futsalmanager.ui.login.email_verification


sealed interface EmailVerificationEffect {
    object OnBackClicked : EmailVerificationEffect
    object Navigate : EmailVerificationEffect
    data class ShowError(val message: String) : EmailVerificationEffect
}