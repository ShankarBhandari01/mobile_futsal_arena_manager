package com.example.futsalmanager.ui.home.booking

import com.example.futsalmanager.domain.model.Courts
import com.example.futsalmanager.domain.model.PaymentMethod
import com.example.futsalmanager.domain.model.Slot
import java.time.LocalDate

sealed interface BookingIntent {
    object BookingButton : BookingIntent
    data class SelectPaymentMethod(val method: PaymentMethod) : BookingIntent
    data class SelectSlot(val slot: Slot) : BookingIntent
    data class SelectCourt(val courtId: Courts) : BookingIntent
    data class SelectDate(val date: LocalDate) : BookingIntent
}
