package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val email: String,
    val newPassword: String,
    val otp: String
)