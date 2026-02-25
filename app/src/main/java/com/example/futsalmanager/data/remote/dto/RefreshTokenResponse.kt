package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
)
