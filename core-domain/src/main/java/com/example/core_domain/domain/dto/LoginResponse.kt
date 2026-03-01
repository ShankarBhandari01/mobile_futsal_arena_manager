package com.example.core_domain.domain.dto


import com.example.core_domain.domain.model.User
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
