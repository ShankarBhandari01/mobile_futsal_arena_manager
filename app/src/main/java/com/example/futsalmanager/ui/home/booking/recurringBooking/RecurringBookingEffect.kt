package com.example.futsalmanager.ui.home.booking.recurringBooking

import com.example.futsalmanager.ui.home.booking.BookingEffect

sealed interface RecurringBookingEffect {
    data class ShowError(val message: String) : RecurringBookingEffect
}