package com.example.futsalmanager.data.remote.dto

import com.example.futsalmanager.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
class RegisterResponse(
    val user: User,
    val message: String
)