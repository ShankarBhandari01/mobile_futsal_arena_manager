package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentResponseDTO(
    val clientSecret: String,
    val paymentIntentId: String
)