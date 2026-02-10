package com.example.futsalmanager.ui.home.booking

import com.example.futsalmanager.domain.model.Arenas

data class BookingState(
    val isLoading: Boolean = false,
    var arena: Arenas? = null,
    val showLogoutDialog: Boolean = false,
    val selectedDate: String = "",
    val errorMessage: String? = null
)