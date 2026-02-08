package com.example.futsalmanager.ui.login

sealed interface AuthIntent {
    data class ToggleMode(val authMode: AuthMode) : AuthIntent

    data class EmailChanged(val email: String) : AuthIntent
    data class PasswordChanged(val password: String) : AuthIntent

    data class FirstNameChanged(val firstname: String) : AuthIntent
    data class LastNameChanged(val lastname: String) : AuthIntent
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthIntent
    data class PhoneChanged(val phone: String) : AuthIntent

    data object SubmitClicked : AuthIntent

    data object ForgotPasswordClicked : AuthIntent
}
