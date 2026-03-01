package com.example.core_domain.domain.dto

import com.example.core_domain.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
class RegisterResponse(
    val user: User,
    val message: String
)