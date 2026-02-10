package com.example.futsalmanager.ui.home.booking

sealed interface BookingIntent {
    object BookingButton : BookingIntent
}