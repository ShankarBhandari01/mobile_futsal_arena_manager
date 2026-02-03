package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val error: String,
    val message: String
)