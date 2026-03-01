package com.example.feature_home.home

import com.example.core_domain.domain.model.Arenas


sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    object NavigateToMarketPlace : HomeEffect
    object NavigateToMyBooking : HomeEffect
    object NavigateToMyProfile : HomeEffect
    object NavigateToLogin : HomeEffect
    object NavigateToLocationSettings : HomeEffect
    data class NavigateToBookingWithArea(val arena: Arenas) : HomeEffect
}

