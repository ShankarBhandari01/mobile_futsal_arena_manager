package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationResponseDTO(
    val booking: Booking,
    val lockExpiresAt: String,
    val message: String,
    val paymentMethod: String
)