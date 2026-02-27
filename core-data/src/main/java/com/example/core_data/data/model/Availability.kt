package com.example.core_data.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Availability(
    val availabilityPercent: Int? = null,
    val availableSlots: Int? = null,
    val computedAt: String? = null,
    val date: String? = null,
    val state: String? = null,
    val tenantId: String? = null,
    val totalSlots: Int? = null
)