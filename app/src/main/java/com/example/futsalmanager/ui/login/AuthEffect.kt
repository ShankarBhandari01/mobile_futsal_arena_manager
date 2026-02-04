package com.example.futsalmanager.ui.login

sealed interface AuthEffect {
    object NavigateToForgotPassword : AuthEffect
    object NavigateToEmailVerification : AuthEffect

    object Navigate : AuthEffect
    data class ShowError(val message: String) : AuthEffect
}

