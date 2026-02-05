package com.example.futsalmanager.ui.login.forget_password

sealed interface ForgetPasswordEffect {
    object OnBackClicked : ForgetPasswordEffect
    data class NavigateToOtp(val email: String, val message: String) : ForgetPasswordEffect
    data class ShowError(val message: String) : ForgetPasswordEffect
}


