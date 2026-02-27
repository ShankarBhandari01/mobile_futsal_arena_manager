package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourtDto(
    val id: String,
    val name: String,
    val description: String,
    val capacity: Int,
    val basePrice: String,
    val slotDurationMinutes: Int,
    val type: String,
    val amenities: List<String>
)