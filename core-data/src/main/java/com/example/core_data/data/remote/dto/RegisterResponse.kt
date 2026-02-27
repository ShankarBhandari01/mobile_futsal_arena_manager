package com.example.core_data.data.remote.dto

import com.example.core_data.data.model.User
import kotlinx.serialization.Serializable

@Serializable
class RegisterResponse(
    val user: User,
    val message: String
)