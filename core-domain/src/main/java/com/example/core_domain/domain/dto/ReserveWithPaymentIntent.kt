package com.example.core_domain.domain.dto

import com.example.core_domain.domain.model.Arenas
import com.example.core_domain.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ReserveWithPaymentIntent(
    var paymentIntentResponseDTO: com.example.core_domain.domain.dto.PaymentIntentResponseDTO,
    var reservationResponseDTO: com.example.core_domain.domain.dto.ReservationResponseDTO,
    val user: User? = null,
    var arenas: Arenas? = Arenas()
)