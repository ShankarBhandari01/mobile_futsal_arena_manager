package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentResponseDTO(
    val clientSecret: String,
    val paymentIntentId: String
)