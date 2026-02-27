package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationResponseDTO(
    val booking: Booking,
    val lockExpiresAt: String,
    val message: String,
    val paymentMethod: String
)