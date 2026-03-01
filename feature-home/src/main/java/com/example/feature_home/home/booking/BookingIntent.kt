package com.example.feature_home.home.booking

import com.example.core_domain.domain.model.Courts
import com.example.core_domain.domain.model.Slot
import com.example.core_domain.domain.model.emum.PaymentMethod
import java.time.LocalDate

sealed interface BookingIntent {

    data class SelectPaymentMethod(val method: PaymentMethod) : BookingIntent
    data class SelectSlot(val slot: Slot) : BookingIntent
    data class SelectCourt(val court: Courts) : BookingIntent
    data class SelectDate(val date: LocalDate) : BookingIntent
    object MakePayment : BookingIntent
}
