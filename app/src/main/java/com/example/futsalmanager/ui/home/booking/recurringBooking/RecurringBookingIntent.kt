package com.example.futsalmanager.ui.home.booking.recurringBooking

import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.emum.Frequency
import com.example.futsalmanager.domain.model.emum.PaymentMethod
import com.example.futsalmanager.domain.model.emum.PaymentStyle
import com.example.futsalmanager.domain.model.Slot
import java.time.DayOfWeek
import java.time.LocalTime

sealed interface RecurringBookingIntent {
    data class OnSlotSelected(val slot: Slot) : RecurringBookingIntent
    data class OnCourtSelected(val court: Courts?) : RecurringBookingIntent
    data class OnTimeSelected(val time: LocalTime) : RecurringBookingIntent
    data class OnDaySelected(val day: DayOfWeek) : RecurringBookingIntent
    data class UpdateFrequency(val frequency: Frequency) : RecurringBookingIntent
    data class UpdateSessionCount(val sessionCount: Int) : RecurringBookingIntent
    data class UpdatePaymentMethod(val paymentMethod: PaymentStyle) : RecurringBookingIntent
    object Submit : RecurringBookingIntent


}