package com.example.futsalmanager.core.utils

import android.text.TextUtils
import android.util.Patterns
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.ui.login.AuthState

object Common {

    fun String.isValidEmail(): Boolean {
        return isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun AuthState.toRegisterRequest(): RegisterRequest {
        return RegisterRequest(
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            password = this.password,
            phone = this.phone
        )
    }
}