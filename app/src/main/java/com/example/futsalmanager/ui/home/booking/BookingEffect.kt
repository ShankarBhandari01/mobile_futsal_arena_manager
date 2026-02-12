package com.example.futsalmanager.ui.home.booking


sealed interface BookingEffect {
    data class ShowError(val message: String) : BookingEffect
}