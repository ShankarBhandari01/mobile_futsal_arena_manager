package com.example.futsalmanager.ui.home

import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.domain.model.LocationModel

sealed interface HomeIntent {
    data class ArenaClicked(val arena: Arenas) : HomeIntent
    data class ViewModeChanged(val viewMode: ViewMode) : HomeIntent
    data class SearchChanged(val query: String) : HomeIntent
    data class DateChanged(val date: String) : HomeIntent
    object MarketPlaceClicked : HomeIntent
    object MyBookingClicked : HomeIntent
    object MyProfileClicked : HomeIntent
    object LogoutClicked : HomeIntent
    object Refresh : HomeIntent
    object EnableLocationClicked : HomeIntent
    object DismissLogoutDialog : HomeIntent
    object ConfirmLogout : HomeIntent
    object LoadNextPage : HomeIntent

    object OnPermissionsGranted : HomeIntent

    object ScreenStarted : HomeIntent
    object ScreenStopped : HomeIntent




}