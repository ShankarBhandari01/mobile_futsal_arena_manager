package com.example.futsalmanager.data.remote.dto

import com.example.futsalmanager.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ReserveWithPaymentIntent(
    var paymentIntentResponseDTO: PaymentIntentResponseDTO,
    var reservationResponseDTO: ReservationResponseDTO,
    val user: User? = null
)