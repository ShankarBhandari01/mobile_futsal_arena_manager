package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationRequestDTO(
    val arenaId: String,
    val courtId: String,
    val endTime: String,
    val idempotencyKey: String,
    val paymentMethod: String,
    val startTime: String
)