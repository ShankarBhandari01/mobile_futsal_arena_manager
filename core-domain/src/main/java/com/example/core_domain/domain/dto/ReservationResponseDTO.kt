package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationResponseDTO(
    val booking: com.example.core_domain.domain.dto.Booking,
    val lockExpiresAt: String,
    val message: String,
    val paymentMethod: String
)