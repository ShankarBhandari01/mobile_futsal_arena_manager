package com.example.futsalmanager.ui.home

sealed interface HomeIntent {
    data class SearchChanged(val query: String) : HomeIntent
    data class DateChanged(val date: String) : HomeIntent
    object MarketPlaceClicked : HomeIntent
    object MyBookingClicked : HomeIntent
    object MyProfileClicked : HomeIntent
    object LogoutClicked : HomeIntent
    object Refresh : HomeIntent
    object EnableLocationClicked : HomeIntent
}