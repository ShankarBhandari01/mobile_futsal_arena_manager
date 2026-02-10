package com.example.futsalmanager.ui.routes

import android.net.Uri

object Routes {
    const val LOGIN_SCREEN = "login_screen"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password_screen"
    const val EMAIL_VERIFY_SCREEN = "email_verify_screen"
    const val PASSWORD_RESET_SCREEN = "password_reset_screen/{email}"
    const val HOME_SCREEN = "home_screen"

    const val PROFILE = "profile_screen"

    const val MARKETPLACE = "marketplace_screen"
    const val BOOKING = "booking_screen/{id}"

    fun passwordResetScreen(email: String) =
        "password_reset_screen/${Uri.encode(email)}"

    fun bookingScreen(id: String) =
        "booking_screen/${Uri.encode(id)}"
}

