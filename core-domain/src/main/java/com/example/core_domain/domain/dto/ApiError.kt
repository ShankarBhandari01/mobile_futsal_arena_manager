package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val error: String,
    val message: String
)