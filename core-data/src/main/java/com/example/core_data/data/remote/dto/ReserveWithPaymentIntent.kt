package com.example.core_data.data.remote.dto

import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ReserveWithPaymentIntent(
    var paymentIntentResponseDTO: PaymentIntentResponseDTO,
    var reservationResponseDTO: ReservationResponseDTO,
    val user: User? = null,
    var arenas: Arenas? = Arenas()
)