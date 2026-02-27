package com.example.feature_home.home.booking

import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.Courts
import com.example.core_data.data.model.Slot
import com.example.core_data.data.model.emum.PaymentMethod
import java.time.LocalDate


data class BookingState(
    val isLoading: Boolean = false,
    val arena: Arenas? = null,
    val courts: List<Courts?> = emptyList(),
    val showLogoutDialog: Boolean = false,

    val selectedDate: LocalDate = LocalDate.now(),
    val displayDate: String = LocalDate.now().toString(),

    val displayStartTime: String = "00:00 AM",
    val displayEndTime: String = "00:00 AM",

    val errorMessage: String? = null,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ONLINE,
    val availableSlots: List<Slot> = emptyList(),
    val selectedSlot: Slot? = null,
    val selectedCourt: Courts? = null
)