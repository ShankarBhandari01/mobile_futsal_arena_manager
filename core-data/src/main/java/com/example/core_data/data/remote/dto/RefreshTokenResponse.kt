package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
)
