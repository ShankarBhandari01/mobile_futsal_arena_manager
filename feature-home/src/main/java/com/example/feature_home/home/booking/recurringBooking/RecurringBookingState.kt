package com.example.feature_home.home.booking.recurringBooking

import com.example.core_data.data.model.Courts
import com.example.core_data.data.model.Slot
import com.example.core_data.data.model.emum.Frequency
import com.example.core_data.data.model.emum.PaymentStyle
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

    val selectedPaymentStyle: PaymentStyle? = PaymentStyle.PayPerSession,
) {
    fun isValid(): Boolean {
        return frequency != null &&
                selectedPaymentStyle != null &&
                sessionCount > 0 &&
                !isLoading
    }

    fun isScreenTwoValid(): Boolean {
        return selectedDay != null &&
                selectedTime != null &&
                !isLoading
    }
}