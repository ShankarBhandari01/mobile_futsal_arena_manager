package com.example.futsalmanager.ui.component

sealed class BannerState {
    object Hidden : BannerState()
    data class Success(val message: String) : BannerState()
    data class Error(val message: String) : BannerState()
}