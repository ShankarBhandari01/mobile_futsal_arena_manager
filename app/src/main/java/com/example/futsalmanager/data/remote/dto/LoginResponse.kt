package com.example.futsalmanager.data.remote.dto


import com.example.futsalmanager.domain.model.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class LoginResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val selectedArena: JsonElement,
    val tenant: JsonElement,
    val user: User
)
