package com.example.futsalmanager.core.ui.states

sealed class BannerState {
    object Hidden : BannerState()
    data class Success(val message: String) : BannerState()
    data class Error(val message: String) : BannerState()
}