package com.example.futsalmanager.ui.home

sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    object NavigateToMarketPlace : HomeEffect
    object NavigateToMyBooking : HomeEffect
    object NavigateToMyProfile : HomeEffect
    object NavigateToLogin : HomeEffect
    object NavigateToLocationSettings : HomeEffect
}

