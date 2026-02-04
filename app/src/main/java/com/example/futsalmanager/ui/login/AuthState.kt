package com.example.futsalmanager.ui.login

data class AuthState(
    val mode: AuthMode = AuthMode.LOGIN,

    // shared
    val email: String = "iamshankarbhandari@gmail.com",
    val password: String = "12345678",

    // register only
    val firstName: String = "",
    val lastName: String = "",
    val confirmPassword: String = "",
    val phone: String = "",

    val loading: Boolean = false,
    val error: String? = null
)
