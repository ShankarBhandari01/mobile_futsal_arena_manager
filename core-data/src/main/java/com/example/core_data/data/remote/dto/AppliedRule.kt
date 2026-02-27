package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppliedRule(
    val id: String,
    val type: String
)