package com.example.futsalmanager.ui.home.booking

import com.example.futsalmanager.data.remote.dto.PaymentIntentResponseDTO
import com.example.futsalmanager.data.remote.dto.ReserveWithPaymentIntent


sealed interface BookingEffect {
    data class ShowError(val message: String) : BookingEffect
    data class ShowPaymentDialog(val paymentIntent: ReserveWithPaymentIntent) : BookingEffect
}


