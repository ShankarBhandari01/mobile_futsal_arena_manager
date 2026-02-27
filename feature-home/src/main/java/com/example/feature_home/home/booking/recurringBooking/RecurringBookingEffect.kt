package com.example.feature_home.home.booking.recurringBooking


sealed interface RecurringBookingEffect {
    data class ShowError(val message: String) : RecurringBookingEffect
}