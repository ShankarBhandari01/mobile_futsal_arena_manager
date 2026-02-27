package com.example.core_data.data.remote.api

// this file contains all the api endpoints
object ApiRegistry {
    // api login
    const val LOGIN = "auth/login"
    const val REGISTER = "auth/register"

    const val LOGOUT = "auth/logout"
    const val REFRESH = "auth/refresh"

    const val FORGOT_PASSWORD = "auth/forgot-password"
    const val RESET_PASSWORD = "auth/reset-password"
    const val VERIFY_EMAIL = "auth/verify-email"

    //booking
    const val COURTS = "courts"
    const val SLOT = "slots"

    // home api
    const val ARENA_LIST = "discover/arenas"

    const val BOOKINGS = "bookings"

    // reserve
    const val RESERVE = "reserve"

    // create payment
    const val CREATE_PAYMENT = "create-payment"


}
