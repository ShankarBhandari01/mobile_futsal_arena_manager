package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val error: String,
    val message: String
)