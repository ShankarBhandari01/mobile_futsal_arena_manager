package com.example.futsalmanager.ui.home.booking.recurringBooking

import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.Frequency
import com.example.futsalmanager.domain.model.PaymentMethod
import com.example.futsalmanager.domain.model.Slot
import java.time.DayOfWeek
import java.time.LocalTime

data class RecurringBookingState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,

    val selectedSlot: Slot? = null,
    val selectedCourt: Courts? = null,
    val frequency: Frequency? = Frequency.WEEKLY,
    val sessionCount: Int = 1,
    val selectedDay: DayOfWeek? = null,
    val selectedTime: LocalTime? = null,

    val selectedPaymentMethod: PaymentMethod? = PaymentMethod.ONLINE,
) {
    fun isValid(): Boolean {
        return frequency != null &&
                selectedPaymentMethod != null &&
                sessionCount > 0 &&
                !isLoading
    }

    fun isScreenTwoValid(): Boolean {
        return selectedDay != null &&
                selectedTime != null &&
                !isLoading
    }
}