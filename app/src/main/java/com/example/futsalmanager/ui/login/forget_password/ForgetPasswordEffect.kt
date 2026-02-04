package com.example.futsalmanager.ui.login.forget_password

sealed interface ForgetPasswordEffect {
    object OnBackClicked : ForgetPasswordEffect
    object Navigate : ForgetPasswordEffect
    data class ShowError(val message: String) : ForgetPasswordEffect
}


