package com.example.feature_home.home

import com.example.core_data.data.model.Arenas


sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    object NavigateToMarketPlace : HomeEffect
    object NavigateToMyBooking : HomeEffect
    object NavigateToMyProfile : HomeEffect
    object NavigateToLogin : HomeEffect
    object NavigateToLocationSettings : HomeEffect
    data class NavigateToBookingWithArea(val arena: Arenas) : HomeEffect
}

