package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetCodeResponse(
    val message: String,
)
