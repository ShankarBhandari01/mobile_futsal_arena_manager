package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentResponseDTO(
    val clientSecret: String,
    val paymentIntentId: String
)