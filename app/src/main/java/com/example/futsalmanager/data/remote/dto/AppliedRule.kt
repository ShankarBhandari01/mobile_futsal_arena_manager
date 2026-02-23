package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppliedRule(
    val id: String,
    val type: String
)